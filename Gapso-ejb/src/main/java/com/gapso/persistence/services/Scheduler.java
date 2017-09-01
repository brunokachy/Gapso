/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.persistence.services;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author Onyedika Okafor
 */
@Singleton
@LocalBean
@Startup
public class Scheduler {

    @Resource
    TimerService timerService;

    @EJB
    private PortalPersistence persistence;

    @PostConstruct
    public void init() {
        long duration = 86400000;
        timerService.createIntervalTimer(1000, duration, new TimerConfig());
    }

    @Timeout
    public void deleteUnfinishedOrders() {
        System.out.println("Clearing Database of Incomplete Orders");
    }
}
