package org.metaservice.api.ns;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.DC;

public class PACKAGE_DEB {
    public static final String NAMESPACE = "http://metaservice.org/ns/package-deb#";

    /**
     * CLASS
     */
    public final static URI PACKAGE;
    public final static URI MAINTAINER_CLASS;

    /*public final static URI PACKAGE_NAME;*/
    public final static URI DESCRIPTION;
    public final static URI ARCHITECTURE;
    public static final URI VERSION;
    public static final URI TITLE;
    public static final URI HOMEPAGE;
    public static final URI MD5SUM;
    public static final URI SHA1;
    public static final URI SHA256;
    public static final URI FILENAME;
    public static final URI MAINTAINER_PROPERTY;
    public static final URI UPLOADER;

    public static final URI DEPENDS;
    public static final URI PRE_DEPENDS;
    public static final URI SUGGESTS;
    public static final URI RECOMMENDS;
    public static final URI CONFLICTS;
    public static final URI BREAKS;
    public static final URI REPLACES;
    public static final URI PROVIDES;

    public static final URI BUILT_USING;

    public static final URI BUILD_CONFLICTS;
    public static final URI BUILD_CONFLICTS_INDEP;
    public static final URI BUILD_DEPENDS;
    public static final URI BUILD_DEPENDS_INDEP;

    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        PACKAGE = factory.createURI(PACKAGE_DEB.NAMESPACE,"Package");
        MAINTAINER_CLASS = factory.createURI(PACKAGE_DEB.NAMESPACE,"Maintainer");
     /*   PACKAGE_NAME = factory.createURI(PACKAGE_DEB.NAMESPACE,"packageName"); */
        DESCRIPTION = factory.createURI(PACKAGE_DEB.NAMESPACE,"description");
        ARCHITECTURE = factory.createURI(PACKAGE_DEB.NAMESPACE,"architecture");
        VERSION = factory.createURI(PACKAGE_DEB.NAMESPACE,"version");
        HOMEPAGE = factory.createURI(PACKAGE_DEB.NAMESPACE,"homepage");
        TITLE = DC.TITLE;
        MD5SUM = factory.createURI(PACKAGE_DEB.NAMESPACE,"md5sum");
        SHA1 = factory.createURI(PACKAGE_DEB.NAMESPACE,"sha1sum");
        SHA256 = factory.createURI(PACKAGE_DEB.NAMESPACE,"sha256sum");
        FILENAME = factory.createURI(PACKAGE_DEB.NAMESPACE,"filename");
        MAINTAINER_PROPERTY = factory.createURI(PACKAGE_DEB.NAMESPACE,"maintainer");
        UPLOADER = factory.createURI(PACKAGE_DEB.NAMESPACE,"uploader");



        DEPENDS = factory.createURI(PACKAGE_DEB.NAMESPACE,"depends");
        PRE_DEPENDS = factory.createURI(PACKAGE_DEB.NAMESPACE,"pre-depends");
        SUGGESTS = factory.createURI(PACKAGE_DEB.NAMESPACE,"suggests");
        RECOMMENDS = factory.createURI(PACKAGE_DEB.NAMESPACE,"recommends");
        CONFLICTS = factory.createURI(PACKAGE_DEB.NAMESPACE,"conflicts");
        BREAKS = factory.createURI(PACKAGE_DEB.NAMESPACE,"breaks");
        REPLACES = factory.createURI(PACKAGE_DEB.NAMESPACE,"replaces");
        PROVIDES = factory.createURI(PACKAGE_DEB.NAMESPACE,"provides");
        BUILT_USING = factory.createURI(PACKAGE_DEB.NAMESPACE,"built-using");
        BUILD_CONFLICTS = factory.createURI(PACKAGE_DEB.NAMESPACE,"build-conflicts");
        BUILD_CONFLICTS_INDEP = factory.createURI(PACKAGE_DEB.NAMESPACE,"build-conflicts-indep");
        BUILD_DEPENDS = factory.createURI(PACKAGE_DEB.NAMESPACE,"build-depends");
        BUILD_DEPENDS_INDEP = factory.createURI(PACKAGE_DEB.NAMESPACE,"build-depends-indep");

    }


}
