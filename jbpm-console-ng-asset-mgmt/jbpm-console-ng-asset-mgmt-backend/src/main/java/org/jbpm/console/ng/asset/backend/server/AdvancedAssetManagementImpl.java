/*
 * Copyright 2012 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.console.ng.asset.backend.server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.guvnor.asset.management.model.BuildProjectStructureEvent;
import org.guvnor.asset.management.model.ConfigureRepositoryEvent;
import org.guvnor.asset.management.model.PromoteChangesEvent;
import org.guvnor.asset.management.model.ReleaseProjectEvent;
import org.guvnor.asset.management.model.ExecuteOperationEvent;

import org.jbpm.console.ng.bd.service.KieSessionEntryPoint;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.DeploymentEvent;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.cdi.Deploy;

@ApplicationScoped
public class AdvancedAssetManagementImpl {

    private static final String GA_ASSET_MGMT = System.getProperty("org.guvnor.assetmgmt.project", "org.guvnor:guvnor-asset-mgmt-project");

    @Inject
    private KieSessionEntryPoint sessionServices;

    private String deploymentId;

    public AdvancedAssetManagementImpl() {
    }

    public void deploymentObserver(@Observes @Deploy DeploymentEvent event) {
        if (event.getDeploymentId().startsWith(GA_ASSET_MGMT)) {
            deploymentId = event.getDeploymentId();
        }
    }

    public void configureRepository(@Observes ConfigureRepositoryEvent event) {
        sessionServices.startProcess(deploymentId, "guvnor-asset-management.ConfigureRepository", event.getParams());
    }

    public void buildProject(@Observes BuildProjectStructureEvent event) {
        sessionServices.startProcess(deploymentId, "guvnor-asset-management.BuildProject", event.getParams());
    }

    public void promoteChanges(@Observes PromoteChangesEvent event) {
        sessionServices.startProcess(deploymentId, "guvnor-asset-management.PromoteAssets", event.getParams());
    }

    public void releaseProject(@Observes ReleaseProjectEvent event) {
        sessionServices.startProcess(deploymentId, "guvnor-asset-management.ReleaseProject", event.getParams());
    }

    public void executeOperation(@Observes ExecuteOperationEvent event) {
        sessionServices.startProcess(deploymentId, "guvnor-asset-management.ExecuteOperation", event.getParams());
    }
}
