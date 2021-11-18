package org.openmrs.module.messages.bundle;

import org.openmrs.ConceptClass;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/**
 * The MetadataBundle which installs 'ITR Message' and 'ITR Message Parameter' Concept Classes.
 */
public class ITRConceptClassesBundle extends VersionedMetadataBundle {

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    installConceptClass("ITR Message", "The class of concepts with the message text.");
    installConceptClass("ITR Message Parameter", "The class of concepts with the message parameters.");
  }

  private void installConceptClass(String name, String description) {
    final ConceptClass newClass = new ConceptClass();
    newClass.setName(name);
    newClass.setDescription(description);
    this.install(newClass);
  }
}
