package com.surevine.neon.service.rest;

import java.util.Collection;

import javax.ws.rs.*;

import com.surevine.neon.service.bean.SkillServiceBean;
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

    @POST
    public void addSkill(SkillServiceBean skillBean) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
        implementation.addSkill(skillBean);
    }

    private void loadServiceFromSpringContext() {
        log.debug("Loading profileService from Spring context");
        implementation = (SkillService) SpringApplicationContext.getBean("skillService");
    }

}
