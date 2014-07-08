package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://usefulinc.com/ns/doap#">doap</a>
 */
public class DOAP{

    public static final String NAMESPACE = "http://usefulinc.com/ns/doap#";

    public static final String PREFIX = "doap";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://usefulinc.com/ns/doap#DarcsRepository<br>
     * "darcs Repository"<br>
     * darcs source code repository.<br>
     */
    public static final URI DARCS_REPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#CVSRepository<br>
     * "CVS Repository"<br>
     * CVS source code repository.<br>
     */
    public static final URI CVSREPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#SVNRepository<br>
     * "Subversion Repository"<br>
     * Subversion source code repository.<br>
     */
    public static final URI SVNREPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#BazaarBranch<br>
     * "Bazaar Branch"<br>
     * Bazaar source code branch.<br>
     */
    public static final URI BAZAAR_BRANCH;


    /**
     * http://usefulinc.com/ns/doap#ArchRepository<br>
     * "GNU Arch repository"<br>
     * GNU Arch source code repository.<br>
     */
    public static final URI ARCH_REPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#Project<br>
     * "Project"<br>
     * A project.<br>
     */
    public static final URI PROJECT;


    /**
     * http://usefulinc.com/ns/doap#HgRepository<br>
     * "Mercurial Repository"<br>
     * Mercurial source code repository.<br>
     */
    public static final URI HG_REPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#GitRepository<br>
     * "Git Repository"<br>
     * Git source code repository.<br>
     */
    public static final URI GIT_REPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#BKRepository<br>
     * "BitKeeper Repository"<br>
     * BitKeeper source code repository.<br>
     */
    public static final URI BKREPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#Repository<br>
     * "Repository"<br>
     * Source code repository.<br>
     */
    public static final URI REPOSITORY;


    /**
     * http://usefulinc.com/ns/doap#Specification<br>
     * "Specification"<br>
     * A specification of a system's aspects, technical or otherwise.<br>
     */
    public static final URI SPECIFICATION;


    /**
     * http://usefulinc.com/ns/doap#Version<br>
     * "Version"<br>
     * Version information of a project release.<br>
     */
    public static final URI VERSION;


////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://usefulinc.com/ns/doap#homepage<br>
     * "homepage"<br>
     * URL of a project's homepage,<br>
     * 		associated with exactly one project.<br>
     */
    public static final URI HOMEPAGE;


    /**
     * http://usefulinc.com/ns/doap#shortdesc<br>
     * "short description"<br>
     * Short (8 or 9 words) plain text description of a project.<br>
     */
    public static final URI SHORTDESC;


    /**
     * http://usefulinc.com/ns/doap#blog<br>
     * "blog"<br>
     * URI of a blog related to a project<br>
     */
    public static final URI BLOG;


    /**
     * http://usefulinc.com/ns/doap#mailing-list<br>
     * "mailing list"<br>
     * Mailing list home page or email address.<br>
     */
    public static final URI MAILING_LIST;


    /**
     * http://usefulinc.com/ns/doap#category<br>
     * "category"<br>
     * A category of project.<br>
     */
    public static final URI CATEGORY;


    /**
     * http://usefulinc.com/ns/doap#service-endpoint<br>
     * "service endpoint"<br>
     * The URI of a web service endpoint where software as a service may be accessed<br>
     */
    public static final URI SERVICE_ENDPOINT;


    /**
     * http://usefulinc.com/ns/doap#release<br>
     * "release"<br>
     * A project release.<br>
     */
    public static final URI RELEASE;


    /**
     * http://usefulinc.com/ns/doap#vendor<br>
     * "vendor"<br>
     * Vendor organization: commercial, free or otherwise<br>
     */
    public static final URI VENDOR;


    /**
     * http://usefulinc.com/ns/doap#implements<br>
     * "Implements specification"<br>
     * A specification that a project implements. Could be a standard, API or legally defined level of conformance.<br>
     */
    public static final URI IMPLEMENTS;


    /**
     * http://usefulinc.com/ns/doap#os<br>
     * "operating system"<br>
     * Operating system that a project is limited to.  Omit this property if the project is not OS-specific.<br>
     */
    public static final URI OS;


    /**
     * http://usefulinc.com/ns/doap#revision<br>
     * "revision"<br>
     * Revision identifier of a software release.<br>
     */
    public static final URI REVISION;


    /**
     * http://usefulinc.com/ns/doap#browse<br>
     * "browse"<br>
     * Web browser interface to repository.<br>
     */
    public static final URI BROWSE;


    /**
     * http://usefulinc.com/ns/doap#screenshots<br>
     * "screenshots"<br>
     * Web page with screenshots of project.<br>
     */
    public static final URI SCREENSHOTS;


    /**
     * http://usefulinc.com/ns/doap#name<br>
     * "name"<br>
     * A name of something.<br>
     */
    public static final URI NAME;


    /**
     * http://usefulinc.com/ns/doap#created<br>
     * "created"<br>
     * Date when something was created, in YYYY-MM-DD form. e.g. 2004-04-05<br>
     */
    public static final URI CREATED;


    /**
     * http://usefulinc.com/ns/doap#developer<br>
     * "developer"<br>
     * Developer of software for the project.<br>
     */
    public static final URI DEVELOPER;


    /**
     * http://usefulinc.com/ns/doap#location<br>
     * "repository location"<br>
     * Location of a repository.<br>
     */
    public static final URI LOCATION;


    /**
     * http://usefulinc.com/ns/doap#maintainer<br>
     * "maintainer"<br>
     * Maintainer of a project, a project leader.<br>
     */
    public static final URI MAINTAINER;


    /**
     * http://usefulinc.com/ns/doap#language<br>
     * "language"<br>
     * ISO language code a project has been translated into<br>
     */
    public static final URI LANGUAGE;


    /**
     * http://usefulinc.com/ns/doap#tester<br>
     * "tester"<br>
     * A tester or other quality control contributor.<br>
     */
    public static final URI TESTER;


    /**
     * http://usefulinc.com/ns/doap#file-release<br>
     * "file-release"<br>
     * URI of download associated with this release.<br>
     */
    public static final URI FILE_RELEASE;


    /**
     * http://usefulinc.com/ns/doap#repository<br>
     * "repository"<br>
     * Source code repository.<br>
     */
    public static final URI REPOSITORY_PROPERTY;


    /**
     * http://usefulinc.com/ns/doap#bug-database<br>
     * "bug database"<br>
     * Bug tracker for a project.<br>
     */
    public static final URI BUG_DATABASE;


    /**
     * http://usefulinc.com/ns/doap#helper<br>
     * "helper"<br>
     * Project contributor.<br>
     */
    public static final URI HELPER;


    /**
     * http://usefulinc.com/ns/doap#license<br>
     * "license"<br>
     * The URI of an RDF description of the license the software is distributed under.<br>
     */
    public static final URI LICENSE;


    /**
     * http://usefulinc.com/ns/doap#platform<br>
     * "platform"<br>
     * Indicator of software platform (non-OS specific), e.g. Java, Firefox, ECMA CLR<br>
     */
    public static final URI PLATFORM;


    /**
     * http://usefulinc.com/ns/doap#download-mirror<br>
     * "download mirror"<br>
     * Mirror of software download web page.<br>
     */
    public static final URI DOWNLOAD_MIRROR;


    /**
     * http://usefulinc.com/ns/doap#audience<br>
     * "audience"<br>
     * Description of target user base<br>
     */
    public static final URI AUDIENCE;


    /**
     * http://usefulinc.com/ns/doap#description<br>
     * "description"<br>
     * Plain text description of a project, of 2-4 sentences in length.<br>
     */
    public static final URI DESCRIPTION;


    /**
     * http://usefulinc.com/ns/doap#documenter<br>
     * "documenter"<br>
     * Contributor of documentation to the project.<br>
     */
    public static final URI DOCUMENTER;


    /**
     * http://usefulinc.com/ns/doap#wiki<br>
     * "wiki"<br>
     * URL of Wiki for collaborative discussion of project.<br>
     */
    public static final URI WIKI;


    /**
     * http://usefulinc.com/ns/doap#old-homepage<br>
     * "old homepage"<br>
     * URL of a project's past homepage,<br>
     * 		associated with exactly one project.<br>
     */
    public static final URI OLD_HOMEPAGE;


    /**
     * http://usefulinc.com/ns/doap#download-page<br>
     * "download page"<br>
     * Web page from which the project software can be downloaded.<br>
     */
    public static final URI DOWNLOAD_PAGE;


    /**
     * http://usefulinc.com/ns/doap#translator<br>
     * "translator"<br>
     * Contributor of translations to the project.<br>
     */
    public static final URI TRANSLATOR;


    /**
     * http://usefulinc.com/ns/doap#programming-language<br>
     * "programming language"<br>
     * Programming language a project is implemented in or intended for use with.<br>
     */
    public static final URI PROGRAMMING_LANGUAGE;


    /**
     * http://usefulinc.com/ns/doap#anon-root<br>
     * "anonymous root"<br>
     * Repository for anonymous access.<br>
     */
    public static final URI ANON_ROOT;


    /**
     * http://usefulinc.com/ns/doap#module<br>
     * "module"<br>
     * Module name of a repository.<br>
     */
    public static final URI MODULE;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        DARCS_REPOSITORY = valueFactory.createURI(NAMESPACE,"DarcsRepository");
        CVSREPOSITORY = valueFactory.createURI(NAMESPACE,"CVSRepository");
        SVNREPOSITORY = valueFactory.createURI(NAMESPACE,"SVNRepository");
        BAZAAR_BRANCH = valueFactory.createURI(NAMESPACE,"BazaarBranch");
        ARCH_REPOSITORY = valueFactory.createURI(NAMESPACE,"ArchRepository");
        PROJECT = valueFactory.createURI(NAMESPACE,"Project");
        HG_REPOSITORY = valueFactory.createURI(NAMESPACE,"HgRepository");
        GIT_REPOSITORY = valueFactory.createURI(NAMESPACE,"GitRepository");
        BKREPOSITORY = valueFactory.createURI(NAMESPACE,"BKRepository");
        REPOSITORY = valueFactory.createURI(NAMESPACE,"Repository");
        SPECIFICATION = valueFactory.createURI(NAMESPACE,"Specification");
        VERSION = valueFactory.createURI(NAMESPACE,"Version");
        HOMEPAGE = valueFactory.createURI(NAMESPACE,"homepage");
        SHORTDESC = valueFactory.createURI(NAMESPACE,"shortdesc");
        BLOG = valueFactory.createURI(NAMESPACE,"blog");
        MAILING_LIST = valueFactory.createURI(NAMESPACE,"mailing-list");
        CATEGORY = valueFactory.createURI(NAMESPACE,"category");
        SERVICE_ENDPOINT = valueFactory.createURI(NAMESPACE,"service-endpoint");
        RELEASE = valueFactory.createURI(NAMESPACE,"release");
        VENDOR = valueFactory.createURI(NAMESPACE,"vendor");
        IMPLEMENTS = valueFactory.createURI(NAMESPACE,"implements");
        OS = valueFactory.createURI(NAMESPACE,"os");
        REVISION = valueFactory.createURI(NAMESPACE,"revision");
        BROWSE = valueFactory.createURI(NAMESPACE,"browse");
        SCREENSHOTS = valueFactory.createURI(NAMESPACE,"screenshots");
        NAME = valueFactory.createURI(NAMESPACE,"name");
        CREATED = valueFactory.createURI(NAMESPACE,"created");
        DEVELOPER = valueFactory.createURI(NAMESPACE,"developer");
        LOCATION = valueFactory.createURI(NAMESPACE,"location");
        MAINTAINER = valueFactory.createURI(NAMESPACE,"maintainer");
        LANGUAGE = valueFactory.createURI(NAMESPACE,"language");
        TESTER = valueFactory.createURI(NAMESPACE,"tester");
        FILE_RELEASE = valueFactory.createURI(NAMESPACE,"file-release");
        REPOSITORY_PROPERTY = valueFactory.createURI(NAMESPACE,"repository");
        BUG_DATABASE = valueFactory.createURI(NAMESPACE,"bug-database");
        HELPER = valueFactory.createURI(NAMESPACE,"helper");
        LICENSE = valueFactory.createURI(NAMESPACE,"license");
        PLATFORM = valueFactory.createURI(NAMESPACE,"platform");
        DOWNLOAD_MIRROR = valueFactory.createURI(NAMESPACE,"download-mirror");
        AUDIENCE = valueFactory.createURI(NAMESPACE,"audience");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
        DOCUMENTER = valueFactory.createURI(NAMESPACE,"documenter");
        WIKI = valueFactory.createURI(NAMESPACE,"wiki");
        OLD_HOMEPAGE = valueFactory.createURI(NAMESPACE,"old-homepage");
        DOWNLOAD_PAGE = valueFactory.createURI(NAMESPACE,"download-page");
        TRANSLATOR = valueFactory.createURI(NAMESPACE,"translator");
        PROGRAMMING_LANGUAGE = valueFactory.createURI(NAMESPACE,"programming-language");
        ANON_ROOT = valueFactory.createURI(NAMESPACE,"anon-root");
        MODULE = valueFactory.createURI(NAMESPACE,"module");
    }
}
