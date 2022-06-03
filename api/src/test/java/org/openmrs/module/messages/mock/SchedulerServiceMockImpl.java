/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.mock;

import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.Task;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.TaskFactory;
import org.openmrs.util.OpenmrsMemento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.SortedMap;

public class SchedulerServiceMockImpl implements SchedulerService {

    private static int instanceNumber;
    private static HashMap<String, TaskDefinition> storedTasks = new HashMap<>();

    public SchedulerServiceMockImpl() {
        instanceNumber++;
    }

    @Override
    public String getStatus(Integer id) {
        return null;
    }

    @Override
    public void onStartup() {
    }

    @Override
    public void onShutdown() {
        storedTasks = new HashMap<>();
        instanceNumber = 1;
    }

    @Override
    public void shutdownTask(TaskDefinition task) throws SchedulerException {
    }

    @Override
    public Task scheduleTask(TaskDefinition taskDefinition) throws SchedulerException {
        taskDefinition.setId(getInstanceNumber());
        storedTasks.put(taskDefinition.getName(), taskDefinition);
        return TaskFactory.getInstance().createInstance(taskDefinition);
    }

    @Override
    public Task rescheduleTask(TaskDefinition task) throws SchedulerException {
        return null;
    }

    @Override
    public void rescheduleAllTasks() throws SchedulerException {
    }

    @Override
    public Collection<TaskDefinition> getScheduledTasks() {
        return null;
    }

    @Override
    public Collection<TaskDefinition> getRegisteredTasks() {
        return new ArrayList<>(storedTasks.values());
    }

    @Override
    public TaskDefinition getTask(Integer id) {
        return null;
    }

    @Override
    public TaskDefinition getTaskByName(String name) {
        return storedTasks.get(name);
    }

    @Override
    public void deleteTask(Integer id) {

    }

    @Override
    public void saveTaskDefinition(TaskDefinition task) {

    }

    @Override
    public SortedMap<String, String> getSystemVariables() {
        return null;
    }

    @Override
    public OpenmrsMemento saveToMemento() {
        return null;
    }

    @Override
    public void restoreFromMemento(OpenmrsMemento memento) {

    }

    @Override
    public void scheduleIfNotRunning(TaskDefinition taskDef) {
    }

    protected int getInstanceNumber() {
        return instanceNumber++;
    }

}
