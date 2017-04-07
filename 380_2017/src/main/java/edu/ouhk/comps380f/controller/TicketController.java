package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.Ticket;
import edu.ouhk.comps380f.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("ticket")
public class TicketController {

    private volatile long TICKET_ID_SEQUENCE = 1;
    private Map<Long, Ticket> ticketDatabase = new LinkedHashMap<>();

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("ticketDatabase", ticketDatabase);
        return "list";
    }

    @RequestMapping(value = "view/{ticketId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("ticketId") long ticketId) {
        Ticket ticket = this.ticketDatabase.get(ticketId);
        if (ticket == null) {
            return new ModelAndView(new RedirectView("/ticket/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("ticketId", Long.toString(ticketId));
        modelAndView.addObject("ticket", ticket);
        return modelAndView;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("add", "ticketForm", new Form());
    }

    public static class Form {

        private String subject;
        private String body;
        private String categories;

        public String getCategories() {
            return categories;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }
        private List<MultipartFile> attachments;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form, Principal principal) throws IOException {
        Ticket ticket = new Ticket();
        if (ticket.getSubject() == null || ticket.getSubject().length() <= 0
                || ticket.getBody() == null || ticket.getBody().length() <= 0
                || ticket.getCategories() == null || ticket.getCategories().length() <= 0) {
            ticket.setId(this.getNextTicketId());
            ticket.setCustomerName(principal.getName());

            ticket.setSubject(form.getSubject());
            ticket.setBody(form.getBody());
            ticket.setCategories(form.getCategories());

            for (MultipartFile filePart : form.getAttachments()) {
                Attachment attachment = new Attachment();
                attachment.setName(filePart.getOriginalFilename());
                attachment.setMimeContentType(filePart.getContentType());
                attachment.setContents(filePart.getBytes());
                if (attachment.getName() != null && attachment.getName().length() > 0
                        && attachment.getContents() != null && attachment.getContents().length > 0) {
                    ticket.addAttachment(attachment);
                }
            }
        }
        this.ticketDatabase.put(ticket.getId(), ticket);
        return new RedirectView("/ticket/view/" + ticket.getId(), true);
    }

    private synchronized long getNextTicketId() {
        return this.TICKET_ID_SEQUENCE++;
    }

    @RequestMapping(
            value = "/{ticketId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("ticketId") long ticketId,
            @PathVariable("attachment") String name) {
        Ticket ticket = this.ticketDatabase.get(ticketId);
        if (ticket != null) {
            Attachment attachment = ticket.getAttachment(name);
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/ticket/list", true);
    }

    @RequestMapping(
            value = "/{ticketId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View deleteAttachment(@PathVariable("ticketId") long ticketId,
            @PathVariable("attachment") String name) {
        Ticket ticket = this.ticketDatabase.get(ticketId);
        if (ticket != null) {
            if (ticket.hasAttachment(name)) {
                ticket.deleteAttachment(name);
            }
        }
        return new RedirectView("/ticket/edit/" + ticketId, true);
    }

    @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("ticketId") long ticketId,
            Principal principal, HttpServletRequest request) {
        Ticket ticket = this.ticketDatabase.get(ticketId);
        if (ticket == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/ticket/list", true));
        }

        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("ticketId", Long.toString(ticketId));
        modelAndView.addObject("ticket", ticket);
        
        ModelAndView modelAndView2 = new ModelAndView("list");
        modelAndView2.addObject("ticketId2", Long.toString(ticketId));
        modelAndView2.addObject("ticket2", ticket);

        Form ticketForm = new Form();
        ticketForm.setSubject(ticket.getSubject());
        ticketForm.setBody(ticket.getBody());
        modelAndView.addObject("ticketForm", ticketForm);

        return modelAndView;
    }

    @RequestMapping(value = "edit/{ticketId}", method = RequestMethod.POST)
    public View edit(@PathVariable("ticketId") long ticketId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Ticket ticket = this.ticketDatabase.get(ticketId);
        if (ticket == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new RedirectView("/ticket/list", true);
        }
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                ticket.addAttachment(attachment);
            }
        }
        this.ticketDatabase.put(ticket.getId(), ticket);
        return new RedirectView("/ticket/view/" + ticket.getId(), true);
    }

    @RequestMapping(value = "delete/{ticketId}", method = RequestMethod.GET)
    public View deleteTicket(@PathVariable("ticketId") long ticketId) {
        if (this.ticketDatabase.containsKey(ticketId)) {
            this.ticketDatabase.remove(ticketId);
        }
        return new RedirectView("/ticket/list", true);
    }

}
