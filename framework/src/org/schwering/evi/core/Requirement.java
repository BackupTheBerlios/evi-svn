package org.schwering.evi.core;

/**
 * Requirements define relations between modules. A module might require one 
 * or more other modules. Not only the existence but also a specific version 
 * might be required .<br>
 * This class defines just <i>one</i> requirement, e.g. just one module 
 * required by some other module.<br>
 * <br>
 * <b>Note:</b> Requirements of modules are defined in the requiring module's
 * JAR's manifest file. Use the {@link ModuleLoader#ATTR_MODULE_REQUIREMENTS}
 * attribute. Probably, this class is not interesting for module developers!
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class Requirement {
	private String moduleId;
	private float version;
	
	/**
	 * Creates a new requirement. In this case, the version is 0.0.
	 * @param moduleId The required module's id.
	 */
	public Requirement(String moduleId) {
		this.moduleId = moduleId;
		this.version = 0.0f;
	}
	
	/**
	 * Creates a new requirement.
	 * @param moduleId The required module's id.
	 * @param version The required module's version.
	 */
	public Requirement(String moduleId, float version) {
		this.moduleId = moduleId;
		this.version = version;
	}
	
	/**
	 * Creates a new requirement.
	 * @param module The required module's class.
	 */
	public Requirement(ModuleContainer module) throws ModuleLoaderException {
		moduleId = module.getId();
		this.version = module.getVersion();
	}
	
	/**
	 * Creates a new requirement.
	 * @param module The required module's class.
	 * @param version The required module's version.
	 */
	public Requirement(ModuleContainer module, float version) 
	throws ModuleLoaderException {
		moduleId = module.getId();
		this.version = version;
	}
	
	/**
	 * Returns the required module's id.
	 * @return The id.
	 */
	public String getId() {
		return moduleId;
	}
	
	/**
	 * Returns the required module's version.
	 * @return The version.
	 */
	public float getVersion() {
		return version;
	}
	
	/**
	 * Checks whether one of the given modules fulfills the requirement.
	 * @param modules The modules which might fulfill the requirement.
	 * @return <code>true</code> if one of the modules fulfills the requirement.
	 */
	public boolean matches(ModuleContainer[] modules) {
		String id;
		for (int i = 0; i < modules.length; i++) {
			if (modules[i] != null) {
				id = modules[i].getId();
				if (moduleId.equals(id)) {
					return modules[i].getVersion() >= version;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns a string representation with the form 
	 * <code>moduleId:moduleVersion</code>.
	 * @return A string representation of this requirement.
	 */
	public String toString() {
		return moduleId +":"+ String.valueOf(version);
	}
	
	/**
	 * Checks whether all requirements of one module are matched by the 
	 * existence of a bunch of modules.
	 * @param module The module whose requirements are checked.
	 * @param loaded The existing modules who might satisfy the requirements.
	 * @return <code>true</code> if the module is satisfied.
	 */
	public static boolean matches(ModuleContainer module, 
			ModuleContainer[] loaded) {
		Requirement[] requirements = module.getRequirements();
		for (int i = 0; i < requirements.length; i++) {
			if (!requirements[i].matches(loaded)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns an exception if the modules in <code>loaded</code> do not 
	 * satisfy the requirements of <code>module</code>. Otherwise, 
	 * <code>null</code> is returned.
	 * @param module The module whose requirements are checked.
	 * @param loaded The existing modules that might satisfy the requirements.
	 * @return <code>null</code> if the module is satisfied, otherwise a 
	 * <code>RequirementException</code> with a stupid message.
	 */
	public static RequirementException getCause(ModuleContainer module,
			ModuleContainer[] loaded) {
		Requirement[] requirements = module.getRequirements();
		boolean returnException = false;
		StringBuffer msg = new StringBuffer();
		msg.append(module +" requires the following modules:\n");
		for (int i = 0; i < requirements.length; i++) {
			if (!requirements[i].matches(loaded)) {
				msg.append("\t"+ requirements[i]);
				returnException = true;
			}
		}
		if (returnException) {
			return new RequirementException(msg.toString());
		} else {
			return null;
		}
	}
}
