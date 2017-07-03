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
package org.jenkinsci.plugins.vsphere.builders;

import com.vmware.vim25.mo.VirtualMachine;
import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import org.jenkinsci.plugins.vsphere.VSphereBuildStep;
import static org.jenkinsci.plugins.vsphere.VSphereBuildStep.VSphereBuildStepDescriptor.getVSphereCloudByName;
import org.jenkinsci.plugins.vsphere.VSphereGuestInfoProperty;
import org.jenkinsci.plugins.vsphere.tools.VSphere;
import org.jenkinsci.plugins.vsphere.tools.VSphereException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 *
 * @author boixmunl
 */
public class AddAnnotation extends VSphereBuildStep{
    private final String vm;
    private final String name;
    private final String value;
    
    @DataBoundConstructor
    public AddAnnotation(String vm, String name, String value) throws VSphereException {
        this.vm=vm;
        this.name=name;
        this.value=value;
    }
    
    public String getVm() {
        return vm;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public void perform(@Nonnull Run<?, ?> run,@Nonnull FilePath filePath,@Nonnull Launcher launcher,@Nonnull TaskListener listener) throws InterruptedException, IOException {
        try {
            addAnotation(run, launcher, listener);
        } catch (Exception e) {
            throw new AbortException(e.getMessage());
        }
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws Exception {
        boolean retVal = false;
        try {
            for (int i = 0; i < retries; i++) {
                retVal = addAnotation(build, launcher, listener);
                if(retVal){
                    break;
                }
                waitForAttemp();
            }
            if(!retVal){
                retVal = addAnotation(build, launcher, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    private boolean addAnotation(final Run<?, ?> run, final Launcher launcher, final TaskListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Extension
	public static final class AddAnnotationDescriptor extends VSphereBuildStepDescriptor {

		public AddAnnotationDescriptor() {
			load();
		}

		public FormValidation doCheckVm(@QueryParameter String value)
				throws IOException, ServletException {

			if (value.length() == 0)
				return FormValidation.error(Messages.validation_required("the VM name"));
			return FormValidation.ok();
		}

		@Override
		public String getDisplayName() {
			return Messages.vm_title_AddAnnotation();
		}

		public FormValidation doTestData(@QueryParameter String serverName,
				@QueryParameter String vm) {
			try {

				if (serverName.length() == 0 || vm.length()==0 )
					return FormValidation.error(Messages.validation_requiredValues());

				VSphere vsphere = getVSphereCloudByName(serverName, null).vSphereInstance();

				if (vm.indexOf('$') >= 0)
					return FormValidation.warning(Messages.validation_buildParameter("VM"));

				VirtualMachine vmObj = vsphere.getVmByName(vm);
				if (vmObj == null)
					return FormValidation.error(Messages.validation_notFound("VM"));

				if (vmObj.getConfig().template)
					return FormValidation.error(Messages.validation_notActually("VM"));

				return FormValidation.ok(Messages.validation_success());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
