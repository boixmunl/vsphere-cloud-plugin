/*
 * Copyright 2017 boixmunl.
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
package org.jenkinsci.plugins;

import hudson.Plugin;
import hudson.model.Descriptor;
import hudson.slaves.ComputerLauncher;
import java.util.List;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.vSphereCloud.DescriptorImpl;
import org.jenkinsci.plugins.vsphere.VSphereBuildStep;

/**
 *
 * @author boixmunl
 */
public class vSphereCloudStart extends Plugin{
    @Override
    public void postInitialize() throws Exception {
        Jenkins.getInstance().getDescriptorList(vSphereCloudSlaveTemplate.class);
       //vSphereCloudSlaveTemplate.DescriptorImpl ds= (vSphereCloudSlaveTemplate.DescriptorImpl)Jenkins.getInstance().getDescriptor(vSphereCloudSlaveTemplate.class);
       //ds.
        
        
//        vSphereCloud vsphereCloud = ;
//        vSphereCloud.shutDownVMifAny();
    }
}
