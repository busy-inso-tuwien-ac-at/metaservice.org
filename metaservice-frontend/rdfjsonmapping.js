var DEB = {};
DEB.root = "http://metaservice.org/ns/package-deb#";
DEB[DEB.root + "architecture"] = "arch";
DEB[DEB.root + "homepage"] = "homepage";
DEB[DEB.root + "description"]="description";
DEB[DEB.root + "version"] = "version";
DEB[DEB.root + "sha256sum"]  = "sha256sum";
DEB[DEB.root + "sha1sum"]  = "sha1sum";
DEB[DEB.root + "md5sum"]  = "md5sum";
DEB[DEB.root + "filename"]  = "filename";
DEB[DEB.root + "maintainer"]  = "maintainer";
DEB[DEB.root + "uploader"]  = "uploader";

DEB["http://purl.org/dc/elements/1.1/title"] = "title";
DEB["http://purl.org/dc/elements/1.1/creator"] = "creator";
DEB["http://purl.org/dc/elements/1.1/date"] = "date";
DEB["http://purl.org/dc/elements/1.1/description"] = "description";
DEB["http://xmlns.com/foaf/0.1/name"] = "name";
DEB["http://xmlns.com/foaf/0.1/mbox"] = "mbox";
DEB["http://purl.org/adms/sw/release"] = "release";
DEB["http://purl.org/adms/sw/package"] = "package";

DEB["http://metaservice.org/ns/metaservice#source"] = "source";
DEB["http://metaservice.org/ns/metaservice#path"] = "path";
DEB["http://metaservice.org/ns/metaservice#time"] = "time";
DEB["http://metaservice.org/ns/metaservice#metadata"] = "metadata";

DEB["http://www.w3.org/1999/xhtml/vocab#next"] = "next";
DEB["http://www.w3.org/1999/xhtml/vocab#prev"] = "prev";


DEB[DEB.root + "depends"]  = "depends";
DEB[DEB.root + "pre-depends"]  = "pre-depends";
DEB[DEB.root + "suggests"]  = "suggests";
DEB[DEB.root + "recommends"]  = "recommends";
DEB[DEB.root + "conflicts"]  = "conflicts";
DEB[DEB.root + "breaks"]  = "breaks";
DEB[DEB.root + "replaces"]  = "replaces";
DEB[DEB.root + "provides"]  = "provides";
DEB[DEB.root + "built-using"]  = "built-using";
DEB[DEB.root + "build-conflicts"]  = "build-conflicts";
DEB[DEB.root + "build-conflicts-indep"]  = "build-conflicts-indep";
DEB[DEB.root + "build-depends"]  = "build-depends";
DEB[DEB.root + "build-depends-indep"]  = "build-depends-indep";
