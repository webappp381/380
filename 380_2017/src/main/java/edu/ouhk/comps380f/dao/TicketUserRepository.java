package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.TicketUser;
import java.util.List;

public interface TicketUserRepository {
    public void create(TicketUser user);
    public List<TicketUser> findAll();
    public TicketUser findByUsername(String username);
    public void deleteByUsername(String username);
}
