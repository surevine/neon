package com.surevine.neon.service.rest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.service.SkillService;
import com.surevine.neon.service.impl.SkillServiceImpl;
import com.surevine.neon.util.SpringApplicationContext;

@Path("/skill")
@Produces("application/json")
@Consumes("application/json")
public class RestSkillService implements SkillService {

    private Logger log = Logger.getLogger(RestSkillService.class);
	
	private SkillService implementation;
	
    public void setImplementation(SkillService implementation) {
		this.implementation = implementation;
	}

	@GET
    @Path("{skillName}/people")
	@Override
	public Collection<ProfileBean> getUsersForSkill(@PathParam("skillName") String skillName, @QueryParam("minLevel") @DefaultValue("0") int minLevel) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.getUsersForSkill(skillName, minLevel);
	}
	
    private void loadServiceFromSpringContext() {
        log.debug("Loading profileService from Spring context");
        implementation = (SkillService) SpringApplicationContext.getBean("skillService");
    }

}
