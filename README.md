<h1><img src="http://enroute.osgi.org/img/enroute-logo-64.png" witdh=40px style="float:left;margin: 0 1em 1em 0;width:40px">
OSGi enRoute Archetype</h1>

This repository represents a template workspace for bndtools, it is the easiest way to get started with OSGi enRoute. The workspace is useful in an IDE (bndtools or Intellij) and has support for [continuous integration][2] with [gradle][3]. 

##Base Tutorial

The *Base Tutorial* is a typical OSGi project. Within this project, a simple server application that interacts with an OSGi console shell is developed.

##Prerequisites

The following software is needed:
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Eclipse Mars](https://www.eclipse.org/downloads/)
* [Git] (https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [Bndtools] (http://bndtools.org/installation.html#update-site) version 3.1.0 or later.

Details on how to install *bndtools* can be found on the web site http://bndtools.org/installation.html

*Bndtools* can  be installed from *Eclipse Marketplace*. Alternatively, it can be installed from:

     https://dl.bintray.com/bndtools/bndtools/latest

You can find the details how to install bndtools on the bndtools website

##The Bnd Workspace

OSGi enRoute requires that the files belonging to a specific project are grouped in a *bnd workspace*. A *Bnd workspace* is a directory with a *cnf* folder and a number of projects. The *cnf* folder contains a *build.bnd* file and a *ext* directory which together define the workspace properties. A *Bnd workspace* is like a module; it imports bundles (and JARs) from a repository and it exports bundles to the same or another repository. On the inside we have projects that are private to the workspace. The projects should be cohesive so that they can share information via the *cnf* project.

###The GitHub workspace

The *GitHub workspace* is intended for sharing on [GitHub](https://github.com). The *bnd* template workspace can be obtained from [GitHub](https://github.com/osgi/workspace).

	 $ cd  ~/git
	 $ git init tech.ghp.eval
	 $ cd tech.ghp.eval
	 $ git fetch --depth=1 https://github.com/GhPTech/workspace master
	 $ git checkout FETCH_HEAD -- .

###The Eclipse workspace

The *Eclipse workspace* stores *metadata* with personal preferences and history, which should not be shared through GitHub. *Bnd* is able to build independent of Eclipse preferences.

In order to create/select an *Eclipse workspace*, select _File/Switch Workspace/Other ..._ fill in the path accordingly. For the present tutorial the *Eclipse Workspace* should be as follows:

*~/eclipse/tech.ghp.eval*

###Workbench view

In order to get the *Workbench* view, push the *Workbench* button from On the top right corner of *Eclipse* window.

###Import the *Bnd Workspace*

The best way to work with projects in Eclipse is to not store them in the Eclipse workspace folder but import them from the *GitHub workspace*.

From the context menu (right click) select 
	 
*@/Import -> General/Existing Projects into Workspace*

then select the corresponding *Bnd Workspace* from *GitHub*. For the present tutorial, the following path:

*~/git/tech.ghp.eval*
    
Finish the import process,by pushing the *Finish* button.

This will import the cnf project into your Eclipse workspace.

####The *Bndtools* perspective

The *Bndtools* perspective gives access to specific *Bnd* tools. For this perspective, from the main menu, select 

*Window/Perspective/OpenPerspective/Bndtools*

##OSGi enRoute project templates

*OSGi enRoute* has a special templates for various projects types:

* API projects *.api*, defines the service 
* Provider projects *.provider*, *.adapter*, implements a project
* Test Projects *.test*, runs tests inside the framework
* Application projects *.application*, bind together a set of components and parametrizes them

###API Project

The first step in creating a service is to define the Application Programming Interface (API). The API  is like a service contract. The best practice is to give service contracts their own projects so they can be easily shared. For API projects, the sufix of the name should be *.api*. OSGi enRoute recognises this suffix and will select the appropriate layout for the API project.

To create the API project, from the main menu select

*File/New/Bndtools Project*

select the *OSGi enRoute* template and give the following name:

*tech.ghp.eval.api*

This creates a project with all the options set to treat it as an API project. The templates already created the *Eval* interface in the right package.


Fill in the following source code:

     package tech.ghp.eval.api;

	 /**
 	  * A service that evaluates an expression
 	  */
	 public interface Eval {
	 	/**
	 	 * Evaluates an expression and returns the result
	 	 */
	
	 	double eval(String expression) throws Exception;
	}

In the *generated* folder of the project, the file

*tech.ghp.eval.api.jar*

was created. This file contains the *Manifest*, which was generated by *bnd*. Have look at the contents.

    Manifest-Version: 1.0
    Bnd-LastModified: 1461049371868
    Bundle-Description: This is a project. An API project should in genera
    l not contain any implementation code.A OSGi enRoute template.
    Bundle-ManifestVersion: 2
    Bundle-Name: tech.ghp.eval.api
    Bundle-SymbolicName: tech.ghp.eval.api
    Bundle-Version: 1.0.0.201604190702
    Created-By: 1.8.0_73 (Oracle Corporation)
    Export-Package: tech.ghp.eval.api;version="1.0.0"
    Require-Capability: compile-only,osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.8))"
 	Tool: Bnd-3.1.0.201512181341


The *Manifest* shows that the package *tech.ghp.eval.api* is exported. This means that this package is available for other bundles. The *exports* can be examined in the *Contents* tab of the*bnd.bnd* file.

The manifest shows how the package *tech.ghp.eval.api* is exported and made available to other bundles.

![com.acme.prime.eval.api](http://enroute.osgi.org/img/tutorial_base/project-create-5.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. The package 'com.acme.prime.eval.api' is exported and made available to other bundles. ")

The *Contents* tab of the *bnd.bnd* file shows the *Export packages* list.   


The *version* of this package is defined in the file *package-info.java*. 

    @org.osgi.annotation.versioning.Version("1.0.0")
    package tech.ghp.eval.api;

The modifications of this package should be reflected in the specified version. Any modification that affects the public API must result in a rebuild of the bundle of the *provider*(the party that implements the *Eval* interface). For example, if the version goes to *1.1*, the bundels that implemented *1.0* should no longer be compatible. In order to assure that the proper version ranges are used, the following annotation should be added:

    @ProviderType
    public interface Eval{ ... }
    
By making this annotation, the *bndtool* will automatically ensure that the *providers/implementers* of the respective interface  use semantic versioning to import the package with a minor range, *1.0, 1.0*.

The default implemented interfaces of the API (usually listener like interfaces) are *consumer* type and could be annotated as

    @ConsumerType
    
The *bnd* project is driven by the *bnd.bnd* file, which defines the *version*, the *build path* and which packages should go into the bundle. For example, the source of the *bnd.bnd* file is as follows:

    Bundle-Version:1.0.0.${tstamp}
    Bundle-Description: 				\
	    This is  project. An API project should in general not contain any \
	    implementation code.\
	    \
	    ${warning;Please update this Bundle-Description in tech.ghp.eval.api/bnd.bnd}

	
     Export-Package:  \
	     tech.ghp.eval.api;provide:=true


     Require-Capability: \
	     compile-only

    -buildpath: \
	     osgi.enroute.base.api;version=1.0

    -testpath: \
	    osgi.enroute.junit.wrapper;version=4.12

###Provider Project

As the API (the service contract) is defined, a provider (an implementation) of the proposed service is required. The *provider* implements the *API* so the users (the clients) can employ the *service*. The *provider projects* have the *.provider* suffix. The root name should be the same as the workspace name. 

To create a *provider* project, from the main menu select

*File/New/Bndtools Project*

select the *OSGi enRoute* template and give the following name:

*tech.ghp.eval.provider*

The *OSGi enRoute* template creates the *EvalImpl.java* file, which includes the *@Component* annotation in order to setup the *Declarative Service (DS)* and the *EvalImpl* class.

####Implementation

As the *EvalImpl* component class implements the *Eval* interface, this implementation needs to be register as an *Eval* service. This registration is specified as follows:

    @Component(name = "tech.ghp.eval")
    public class EvalImpl implements Eval { }

####Build Path

As the *Eval* interface is not a part of the *provider project*, the compilation and the build of the code cannot be carried out. A *build path* to the *api project* has to be provided to the *bnd.bnd* file.

The *buildpath* can be edited with the *Build* tab of the *bnd.bnd* file by adding the  *tech.ghp.eval.api* bundle. Alternatively, the *buildpath* can be edited with the *Source* tab as 

    -buildpath: \
	    osgi.enroute.base.api;version=1.0,\
	    tech.ghp.eval.api;version=latest

####Imports

The *bnd.bnd* files defines the dependencies of the bundle. It consists of 

* *Private packages*: packages only available the current bundle;
* *Exported packages*: packages exported to other bundles;
* *Imported packages*: packages provided to the current bundle;

In order to recognise the *Eval* type, the implementation class *EvalImpl* has to import the package *tech.ghp.eval.api* to get the *Engineering* interface.

    import tech.ghp.engineering.api.Eval;

In this case, the user of the *Eval* service would depend on two bundles: *tech.ghp.eval.api* and *tech.ghp.eval.provider*. 

![com.acme.prime.eval.api/com.acme.prime.eval.provider](http://enroute.osgi.org/img/tutorial_base/provider-imports-1.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. The bundle is importing the *com.acme.prime.eval.api* package to get the *Eval* interface.")

To avoid this double dependency, the *API* bundle can be exported to the *provider* bundle. In order to export the *API* bundle, add the package *tech.ghp.eval.api* to the *Export Packages* of the 'bnd.bnd' file of the provider project. 

![com.acme.prime.eval.api/com.acme.prime.eval.provider](http://enroute.osgi.org/img/tutorial_base/provider-imports-3.png "The box with rounded corners represents a bundle; the inside black box represents an exported package. By exporting the *com.acme.prime.eval.api*, the bundle *com.acme.prime.eval.provider* is independent.")

####Code

The *Engineering* interface specifies the *eval* method as follows

    public interface Eval {
	     double eval(String expression) throws Exception;
    }

This method needs to be provided by the *EvalImpl* class. 

To begin with, evaluation of trivial additions and subtractions of constants are provided as follows:

    @Component(name = "tech.ghp.eval")
    public class EvalImpl implements Eval {
	        Pattern EXPR = Pattern.compile("\\s*(?<left>\\d+)\\s*(?<op>\\+|-)\\s*(?<right>\\d+)\\s*");
	        @Override
			public double eval(String expression) throws Exception {
				Matcher m = EXPR.matcher(expression);
				if ( !m.matches())
					throw new IllegalArgumentException("Invalid expression " + expression);
		
				double left = Double.valueOf( m.group("left"));
				double right = Double.valueOf( m.group("right"));
				switch( m.group("op")) {
					case "+": return left + right;
					case "-": return left - right;
				}
				return Double.NaN;
			}
	}

The *provider* can both implement and/or be a *client* of the interfaces in a *service* package. The *provider* and the *client* roles have consequences on the versioning of the packages. Any change in the *service* contract (API) require an update of the *privider* (implementation).

The best practice in OSGi for a *provider* is to include *service* (API) codes and export it. However, the *provider* code and the *api* codes should not be part of the same project because the compilation of the *api* would expose the *provider* (implementation) code.

###Testing the Provider with (Standard) JUnit

The implementation (the provider) of the service should be tested before it is released. Testing saves time and resources in the later phases of the development process.  

####JUnit

A *provider* should always have *unit* (white box) tests. These tests have access to private information about the implementation details and proprieties of the components that are not part of the public API. When these tests fail they prohibit the release of the project. 

The *OSGi enRoute template* includes a test case in the *test* directory. The *EvalImplTest* test is placed in the same package as the *EvalImpl* class, but is not part of the bundle. As it is in the same package, it has access at the private information of the *EvalImpl* class. 

For the present template a simple test will be run as follows:

    package tech.ghp.eval.provider;
    
    import junit.framework.TestCase;

    public class EvalImplTest extends TestCase {
    		
    		public void testSimple() throws Exception {
					EvalImpl t = new EvalImpl();
					assertEquals( 3.0,  t.eval("1 + 2"));
			}
			
	}

To run the test from the *context menu* (right click) select

*@Run As/JUnit Test*    

For debugging, *breakpoints* can be inserted at the code lines where the running programe should stop. Tu run a test in *debug* mode, select

*@Debug As/JUnit Tests*

The *JUnit* runner creates a new *VM* with the same *build-path* as the class it tests. All the tests dependencies must be on the *build-path* in order to run the tests. 

* *JUnit 4.x* requires annotations on the test class. Extensions of *TestCase* in the class are not necessary (annotations are ignored).
* *JUnit 3.x* must extend *TestCase* (usually the easiest way to write tests)

##Running the Code

As the API *service* is successfully implemented and tested by the *provider*,  it is ready to be run and called in a framework. 

In OSGi this is usually done by creating a command in the *Apache Felix Gogo Shell*. If given permission, the *Gogo* shell can call a given method of a given service. The permission is granted by adding *service proprieties* as follows:

    @Component(
			name = "tech.ghp.eval", 
			property = {
				Debug.COMMAND_SCOPE + "=test", 
				Debug.COMMAND_FUNCTION + "=eval" 
			}
	) 

These *service proprieties* register the *test:eval* command with Gogo. The *scope* part ('test') is required to disambiguate commands (in case the 'eval' command already exists within *Gogo* bundle).

The *Runtime Environment* defines what *OSGi Framework* should run, how the *framework* should be configured, what support bundles should be loaded and some other parameters. 

The *Run* tab of the *bnd.bnd* file is designed to set this information and consists of the following *UI* parts:

* Core Runtime: allows to select the *framework* and an *execution environment*. 

* Run Requirements: specifies the requirements used for a set of runtime bundles from available repositories. The assembly of these bundles is *resolved* out by the *bndtools*.

For the present template the *framework* should be set to 'org.eclipse.org' (Eclipse Equinox) and the *execution environment* to 'JavaSE-1.8'.

The *Run Requirements* contains by default the current bundle 'tech.ghp.eval.provider'. 

In order to call the 'Eval' service, the command shell provided by *Apache Felix* is employed. For this purpose the bundle 'org.apache.felix.gogo.shell' has to be included in the *Run Requirements*. 

    -runrequires: \
		osgi.identity;filter:='(osgi.identity=tech.ghp.eval.provider)',\
		osgi.identity;filter:='(osgi.identity=org.apache.felix.gogo.shell)'

The required bundles are analysed and assembled by *bndtools* (at the push of the *Resolve* button), which tries to satisfy the requirements. 

The 'Resolution Results', the 'Optional Resources' and the 'Reasons' of the resolution are presented. These bundles are added to the *Run Bundles* list. 

    -runbundles: \
		tech.ghp.engineering.provider;version=snapshot,\
		org.apache.felix.configadmin;version='[1.8.6,1.8.7)',\
		org.apache.felix.gogo.runtime;version='[0.16.2,0.16.3)',\
		org.apache.felix.gogo.shell;version='[0.10.0,0.10.1)',\
		org.apache.felix.log;version='[1.0.1,1.0.2)',\
		org.apache.felix.scr;version='[2.0.0,2.0.1)',\
		org.eclipse.equinox.metatype;version='[1.4.100,1.4.101)',\
		org.osgi.service.metatype;version='[1.3.0,1.3.1)'

###Launching

The application can be launched by pushing the *Run OSGi* or *Debug OSGi* button of the *Run* tab of the *bnd.bnd* file. 

In the 'Gogo shell' of the *Eclipse* console type the following command:

	 *g! test:eval 2+2*

The result of this simple evaluation command is displayed on the following line of the *Gogo shell*

	 *4.0*

###Updating

It is possible to change the source code while the application is running. For example, if more opperations are added to the *EvalImpl* class:

    Pattern EXPR = Pattern.compile("\\s*(?<left>\\d+)\\s*(?<op>\\+|-|\\*|/)\\s*(?<right>\\d+)\\s*");
    ....
    switch (m.group("op")) {
		case "+": return left + right;
		case "-": return left - right;
		case "*": return left * right;
		case "/": return left / right;
	}

As the source code is changed, the *IDE* compiles the class and the *bnd* builds the bundle. A changed bundle is detected by the launcher and the updated version is deployed to the running framework.

When the application is launched, the launcher is started in a remote process and gets the 'proprieties' file created by *bndtools* (what framework to use, what bundles to load, etc.). Once the launcher has initialised everything, it starts the bundles and watches the proprieties file. If the proprieties file changes, the launcher reloads and calculates a *delta* and applies it to the running framework (it does not work for all information). 

##Debugging

###Breakpoints

Debugging can be done by including *breakpoints* on the lines where the code should stop during execution. This allows to follow *variable's* *names* and *values* at various phases of the execution process.

###Apache Felix Web Console

Another debugging tool is the *Apache Felix Web Console*. It provides insights on the running service. 

The *Apache Felix Web Console* is available on the list of *Repositories* under the name 'osgi.enroute.webconsole.xray.provider'. To use it, this bundle has to be included in the *Run Requirements* list. This *plugin* drags in the *web console*, the *Jetty web server*, *XRay*, and other required bundles. At the push of the *Resolve* button, the required bundles are analysed and assembled  by *bndtools*. As a result the following bundels are added to the the *Run Bundles* list: 

    -runbundles: \
    	...
    	osgi.enroute.bostock.d3.webresource;version='[3.5.6,3.5.7)',\
		osgi.enroute.dto.bndlib.provider;version='[1.0.0,1.0.1)',\
		osgi.enroute.executor.simple.provider;version='[1.0.0,1.0.1)',\
		osgi.enroute.logger.simple.provider;version='[1.1.1,1.1.2)',\
		osgi.enroute.web.simple.provider;version='[1.3.3,1.3.4)',\
		osgi.enroute.webconsole.xray.provider;version='[2.0.0,2.0.1)'

The *web console* can be accessed with a browser at the following address:

	 http://localhost:8080/system/console/xray

The *username* is *admin* and the *password* is *admin*. 

####XRay

A visualisation of the service layer can be obtained with the *XRay* web console plugin. 

![XRay](http://enroute.osgi.org/img/tutorial_base/debug-xray-1.png "A visualisation of the service layer can be obtained with the *XRay* web console plugin. ")

The *triangle* represents the *service*. Connections to the *point* of the triangle indicate the *registration* (points to the object that receives the method calls from the service users). The *base* of the triangle indicate the *client(s)* (call the methods of the service). The *sides* of the triangle indicate *listeners* of the service.

![service symbols](http://enroute.osgi.org/img/tutorial_base/debug-service-0.png "Graphical representation of services in XRay")

The following color codes are used:

* yellow: service is registered and in use
* white, solid border: service is registered and not in use
* white, dashed border: service is not registered and needed by other bundles
* red: serious failure of the service
* orange: service is exported or imported  

The *triangles* represent service *types*, not *instances*.

*XRay* also tracks eventual errors logged by the bundles. It marks the bundle with a warning sign and the log message.

Various *tabs* and *subtabs* can be selected in order to get an overview of the running framework:

* Main
	* HTTP Service
	* X-Ray
* OSGi
	* Bundles
	* Configurations
	* Log Service
	* Services
* Web Console 
	* Licenses
	* System Information
	
The *XRay* plugin returns a page with *Javascript* that pulls the server at given time intervals. The server returns the data which is rendered by a javascript library (d3.js).

Each service type is represented by a rounded corner yellow box, with 

##Tesing in OSGi

In order to test the *service contracts*, *OSGi JUnit tests* are run. A sperate project is needed for testing. The contentent of these tests should be available to other projects. 

Select *New/Bndtools Project/*, choose *OSGi enRoute* template and name it 'tech.ghp.eval.test'. 

The *OSGi enRoute* template provides a simple test case with *Bundle Context*, but does not include the *Eval* service.

    package tech.ghp.eval.test;
    
    import org.junit.Assert;
    import org.junit.Test;
    import org.osgi.framework.BundleContext;
    import org.osgi.framework.FrameworkUtil;
    
    public class EvalTest {

    private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    
    @Test
    public void testEval() throws Exception {
    	Assert.assertNotNull(context);
    	}
    }

In order to include the *Eval* service the following code is needed:

    <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

Assertion of service existence is also needed:

    Assert.assertNotNull(getService(Eval.class));

As the *Eval* class is recognised by the current project, the *tech.ghp.eval.api* project needs to be included on the *BuildPath* of the *bnd.bnd* file (*tech.ghp.eval.test* project). 
Also *import* of the *Eval* and the *ServiceTracker* classes need to be done into the source code *EvalTest* file.

    import org.osgi.util.tracker.ServiceTracker;
    import tech.ghp.eval.api.Eval;

Finally, the dependencies need to be *resolved* from the *Run* tab of the *bnd.bnd* file. As a result the following bundles will be added into the *Run Bundles* list:

    -runbundles: \
		tech.ghp.eval.provider;version=snapshot,\
		tech.ghp.eval.test;version=snapshot,\
		org.apache.felix.configadmin;version='[1.8.6,1.8.7)',\
		org.apache.felix.log;version='[1.0.1,1.0.2)',\
		org.apache.felix.scr;version='[2.0.0,2.0.1)',\
		org.eclipse.equinox.metatype;version='[1.4.100,1.4.101)',\
		org.osgi.service.metatype;version='[1.3.0,1.3.1)',\
		osgi.enroute.hamcrest.wrapper;version='[1.3.0,1.3.1)',\
		osgi.enroute.junit.wrapper;version='[4.12.0,4.12.1)'

To run the test select the 'bnd.bnd' file and from the *context menu* (right click) select 

*@/Debug As/Bnd OSGi Test Launcher (JUnit)*

As it is, the test only checks if the service exists. Other tests can be added as necessary. For example, to check if *2+2=4*, the following test is added:

    @Test
    	public void sum() throws Exception {
    		Assert.assertSame( 4, (int)getService(Eval.class).eval("2+2"));
    	}

This test case can be run separately from *context menu* of *sum* function as *Debug As/Bnd OSGi Test Launcher (JUnit)*.

Test bundles are marked with the heander *Test-Cases*. This header contains a list with class names that contain *JUnit* tests. The *Source* tab of the 'bnd.bnd' file contains the header 

    Test-Cases: ${test-cases}

The *${test-cases}* macro is set by OSGi enRoute template, which either extend the *junit.framework.TestCase* class or use the JUnit 4 annotations like *@Test*. In the present example, the expansion is as follows (generated/com.acme.prime.eval.test.jar):

     Test-Cases: tech.ghp.eval.test.EvalTest
     
When a OSGI JUnit test is launched, bnd creates a new framework with the set run bundles. On the class path (for the current framework), bundle *aQute.junit* is added as well as the JARs listed on the *-testpath*. The features available for running a *usual framework* are also available for running a *test framework*, e.g. *-runproprieties*, *-runtrace*.

Once all bundles are started, the *aQute.junit* bundle searches for the header *Test-Cases*, loads the classes and run the specified tests.

If the tests are run from *Eclipse JUnit* framework, the *bnd* sets up a new framework and passes a set of classes/methods that Eclipse has chosen from a given selection. The *aQute.junit* will then execute only those classes/methods and report the results back to Eclipse for the JUnit view.

##Deploying the Application

The provider bundle can be used as a *deployable* application. 

From the main menu select *New/Bndtools OSGi Project*, the *OSGi enRoute* template and name the project 'tech.ghp.iot.application'. 

An *.application* project should contain limited amounts of code concerning the requirement that drives the final application.

By default, an application only contains a *Gogo shell command* (in this example *IotApplication* class). In the *EvalImpl* class, the existing API call *eval*, was made available as a *Gogo shell command*. In the present *application* project, the service and the call to the method of the service need to be obtained.

Getting a service is facilitated by *DS*; the *@Reference* annotation and a settler method and need to be added as follows:

    @Component(
			service=EvalApplication.class, 
			property = { 
					Debug.COMMAND_SCOPE + "=eval",
					Debug.COMMAND_FUNCTION + "=eval" 
			},
			name="tech.ghp.eval"
	)
	public class EvalApplication {
			private Eval eval;

			public double eval(String m) throws Exception {
					return eval.eval(m);
			}
			
		@Reference
		void setEval( Eval eval) {
				this.eval= eval;
		}
	}

This class provides a dummy service that provides *eval:eval* command to Gogo shell (notice the difference with the command *test:eval* created in the provider project).

As with the provider project, the dependency on the API project has to added on *buildpath* as

    -buildpath: \
		osgi.enroute.base.api,\
		tech.ghp.eval.api;version=latest 

The *Eval* type from the API project should aslo be included as 

    import tech.ghp.eval.api.Eval;

###Defining the Application

The runtime requirements are defined in a special file created in the application project: 

*tech.ghp.eval.bndrun* 

This file contains information concerning the *Run Requirements*. By default the *Run Requirements* list containts the 'com.acme.prime.eval.application' bundle. For interaction with the *Eval* service, the bundle *org.apache.felix.gogo.shell* bundle should be added from the *Repositories* list to the *Run Requirements* list and the *Resolve* button should be pushed to identify and assemble the *Run Bundles*. 

To check if the application runs from the *Run* tab of the *tech.ghp.eval.bndrun* file push the *Debug OSGi* button. This will launch the framework and run the application.

On the shell command try the following a simple (sum, subtraction, multiplication, division) evaluation:

    g! eval:eval 7+7

###Debugging

A debugging friendly framework can also be used. The *application* project also contains the file *debug.bndrun* file. This file includes the *tech.ghp.eval.bndrun* file and it adds debug requirements. 

To try this *Degugging* mode of running the application, open the file *debug.bndrun* file, resolve the dependencies by pushing the *Resolve* button, then push the *Debug OSGi* button from the upper right side of the Eclipse console.  This will launch the application and display on the shell supplementary information about the runtime. This is triggered by the *-runtrace* flag. This can be set to *false* or removed, if this information is not required.

A set debugging tools such as *Web console*, *Gogo shell*, *XRay*, etc. are available. The running system can be inspected on a browser at the address:

*http://localhost:8080/system/console/xray*

The principle 'Don't Repeat Yourself (DRY)' is applied. The file *debug.bndrun* inherits from file *tech.ghp.eval.bndrun*, i.e. information already available in the later file is aslo available in the former file.

###Executable

In order to deploy the application on an other environment, an executalbe JAR is generated.

From the *Run* tab of the 'tech.ghp.eval.bndrun' file push the *Export* button and specify the path of the executable JAR.

Copy the JAR into the target environment, open a terminal and check the Java version with the command

    java -version
    
move to the location where the executable JAR was copied

    cd ~/[patch to the location of the executable JAR]
   

and launch the application with the command

    java -jar tech.ghp.eval.jar

From the command line of the Apache Felix Gogo shell the 'Eval' service can be used with the commands

    eval:eval 8*6
       
##Integration

The current application *tech.ghp.eval* is build by Eclipse IDE. This is useful for local usage and development, however the philosophy of *open source* and *collaborative development* should be considered. Moreover, when application building is carried out on the personal system, many dependencies accumulate. These dependencies are not suitable for *collaborative development* and *open source*. Automatic building from the command line should also be carried out.

###Automatic building

*OSGi enRoute* includes a full *Gradle* build. The *gradlew* script downloads the appropriate *gradle* version (Java version 1.8 should be available):

    $ cd /Users/aqute/git/tech.ghp.eval
    $ java -version
    java version "1.8.0"
    Java(TM) SE Runtime Environment ...
    $ ./gradlew
    Downloading https://b../biz.aQute.bnd-latest.jar to 
    /Users/aqute/git/tech.ghp/cnf/cache/biz.aQute.bnd-latest.jar ... 
    :help

The possible *gradle tasks* can be obtained with the command
    
    $./gradlew tasks

To automatically builds the application 'tech.ghp.eval' from the command line, the following command is used:

    $./gradlew export.tech.ghp.eval
    
This will create and store the JAR *tech.ghp.eval.jar* on the path 
*tech.ghp.eval.api/generated/distributions/executable/*
*tech.ghp.eval.api/generated/*

###Sharing

In order share the development, the project can be pushed to the *GitHub*. 

On the GitHub home page [GitHub](https://github.com/GhPTech), a new repository has to be created. Name this repository *tech.ghp.eval*, add a short description 'An example workspace fro the osgi.enroute base tutorial' and set the repository as 'Public' (do not set initialise the 'README' file, do no add 'gitnore' and license).

![GitHub Repository](http://enroute.osgi.org/img/tutorial_base/ci-github-1.png "Create a New Repository dialog")


In order to connect to the local repository *!/git/com.acme.prime.eval*, the 'SSH URI' *git@github.com:GhPTech/tech.ghp.eval.git* needs to be copied, then on the command line the following commands should be launched:

    $ cd ~/git/tech.ghp.eval
	$ git add .
	$ git commit -m "first commit"
	$ git remote add origin git@github.com:GhPTech/tech.ghp.eval.git
	$ git push -u origin master

For further commits and pushes the following commands are used:

    $ git add .
	$ git commit -m "[commit message]"
	$ git push -u origin master

###Continous Integration

The *bnd workspace* is setup to be built continuously with *Travis CI*. 

From the *Travis CI* website [https://travis-ci.org](https://travis-ci.org "Travis CI website") *create an account* based on GitHub credentials (or *login*). 

Once logged in, select the *Repositories* tab. The *Sync Now* button should be pushed in order to update the latests changes from *GitHub*. Find the 'com.acme.prime' repository and push the *ON* button. Every push will now be automatically build the repository 'com.acme.prime' on the *Travis IC* server. 

In order to trigger the build on *Travis IC* server, a change in the *tech.ghp.eval* repository is made. The warning from the 'bnd.bnd' file of the provider project is commented as follows:

    Bundle-Version:					1.0.0.${tstamp}
    Bundle-Description: 				\
			A bundle with a provider. Notice that this provider exports the API package. \
			It also provides a JUnit test and it can be run standalone. \
			\
			#${warning;Please update this Bundle-Description in tech.ghp.eval.provider/bnd.bnd}

The change is *saved* and *pushed* to the *GitHub* server. The *Travis IC* notices the difference and launches a new automatic build of the repository.

[1]: http://enroute.osgi.org/quick-start.html
[2]: http://enroute.osgi.org/tutorial_base/800-ci.html
[3]: https://www.gradle.org/
