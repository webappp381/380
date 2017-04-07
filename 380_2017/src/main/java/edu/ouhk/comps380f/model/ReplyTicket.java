package edu.ouhk.comps380f.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReplyTicket {

    private long id;
    private long refTicketid;
    private String replybody;
 
    private Map<String, Attachment> attachments = new LinkedHashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRefTicketid() {
        return refTicketid;
    }

    public void setRefTicketid(long refTicketid) {
        this.refTicketid = refTicketid;
    }

    public String getReplybody() {
        return replybody;
    }

    public void setReplybody(String replybody) {
        this.replybody = replybody;
    }

    public Attachment getAttachment(String name) {
        return this.attachments.get(name);
    }

    public Collection<Attachment> getAttachments() {
        return this.attachments.values();
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments() {
        return this.attachments.size();
    }

    public boolean hasAttachment(String name) {
        return this.attachments.containsKey(name);
    }

    public Attachment deleteAttachment(String name) {
        return this.attachments.remove(name);
    }

}
