/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.ReplyTicket;
import edu.ouhk.comps380f.model.Ticket;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import edu.ouhk.comps380f.controller.TicketController;

@Controller
@RequestMapping("reply")
public class ReplyTicketController {
     private volatile long REPLYTICKET_ID_SEQUENCE = 1;
   
       
     @RequestMapping(value = "{ticketId}", method = RequestMethod.GET)
    public ModelAndView replyView(@PathVariable("ticketId") long ticketId) {
        //Ticket ticket = this.ticketDatabase.get(ticketId);

        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("ticketId", Long.toString(ticketId));
       return new ModelAndView("reply", "replyForm",new ReplyForm());
    }
    
       public static class ReplyForm {

        private Long refticketId;
        private String replybody;

        private List<MultipartFile> replyattachments;

        public Long getRefticketId() {
            return refticketId;
        }

        public void setRefticketId(Long refticketId) {
            this.refticketId = refticketId;
        }
      
      
        public String getReplybody() {
            return replybody;
        }

        public void setReplybody(String replybody) {
            this.replybody = replybody;
        }

        public List<MultipartFile> getReplyattachments() {
            return replyattachments;
        }

        public void setReplyattachments(List<MultipartFile> replyattachments) {
            this.replyattachments = replyattachments;
        }
    }
       
        @RequestMapping(value = "{ticketId}", method = RequestMethod.POST)
    public View reply(ReplyForm form,@PathVariable("ticketId") long ticketId) throws IOException {
        ReplyTicket replyTicket = new ReplyTicket();
        replyTicket.setId(this.getNextReplyTicketId());
        replyTicket.setRefTicketid(ticketId);
        replyTicket.setReplybody(form.getReplybody());
        
        for (MultipartFile filePart : form.getReplyattachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                replyTicket.addAttachment(attachment);
            }
        }
        TicketController.replyTicketDatabase.put(replyTicket.getId(), replyTicket);
        Ticket ticket = TicketController.ticketDatabase.get(ticketId);
        ticket.setReplyId(replyTicket.getId());
        
        TicketController.ticketDatabase.replace(ticketId,ticket);
        return new RedirectView("/ticket/view/" + ticketId, true);
    }
    
    private synchronized long getNextReplyTicketId() {
        return this.REPLYTICKET_ID_SEQUENCE++;
    }
}
