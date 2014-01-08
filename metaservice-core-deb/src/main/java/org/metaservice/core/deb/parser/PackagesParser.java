package org.metaservice.core.deb.parser;

import org.jetbrains.annotations.NotNull;
import org.metaservice.core.deb.parser.ast.*;
import org.metaservice.core.deb.parser.ast.Package;
import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specification: http://www.debian.org/doc/debian-policy/ch-controlfields.html
 */
@BuildParseTree
public class PackagesParser extends BaseParser<Object> {
    Logger LOGGER = LoggerFactory.getLogger(PackagesParser.class);
        public Rule List() {
            return Sequence(
                    push(new SuperNode()), // LIST
                    OneOrMore(
                            Package(),    //EXPECT PACKAGE
                            addAsChild(),
                            PackageEnd()
                            )
                    , BaseParser.EOI);
        }

        Rule Package(){
            Var<SuperNode> node = new Var<>();
            return Sequence(
                    push(new Package()),  //PUSH PACKAGE
                    OneOrMore(
                            FirstOf(
                                    Sequence(node.set(new Entries.Package()), SimpleEntry(node, "Package", PackageIdentifier())),
                                    Sequence(node.set(new Entries.Source()), SimpleEntry(node, "Source", PackageIdentifier())),

                                    Sequence(node.set(new Entries.Depends()), SimpleEntry(node, "Depends", DependencyList())),
                                    Sequence(node.set(new Entries.PreDepends()), SimpleEntry(node, "Pre-Depends", DependencyList())),
                                    Sequence(node.set(new Entries.Recommends()), SimpleEntry(node, "Recommends", DependencyList())),
                                    Sequence(node.set(new Entries.Suggests()), SimpleEntry(node, "Suggests", DependencyList())),
                                    Sequence(node.set(new Entries.Breaks()), SimpleEntry(node, "Breaks", DependencyList())),
                                    Sequence(node.set(new Entries.Conflicts()), SimpleEntry(node, "Conflicts", DependencyList())),
                                    Sequence(node.set(new Entries.Provides()), SimpleEntry(node, "Provides", DependencyList())),
                                    Sequence(node.set(new Entries.Replaces()), SimpleEntry(node, "Replaces", DependencyList())),

                                    Sequence(node.set(new Entries.Enhances()), SimpleEntry(node, "Enhances", DependencyList())),
                                    Sequence(node.set(new Entries.BuildDepends()), SimpleEntry(node, "Build-Depends", DependencyList())),
                                    Sequence(node.set(new Entries.BuildDependsIndep()), SimpleEntry(node, "Build-Depends-Indep", DependencyList())),
                                    Sequence(node.set(new Entries.BuildConflicts()), SimpleEntry(node, "Build-Conflicts", DependencyList())),
                                    Sequence(node.set(new Entries.BuildConflictsIndep()), SimpleEntry(node, "Build-Conflicts-Indep", DependencyList())),

                                    Sequence(node.set(new Entries.BuiltUsing()), SimpleEntry(node, "Built-Using", DependencyList())),

                                    Sequence(node.set(new Entries.Version()), SimpleEntry(node, "Version", VersionIdentifier())),
                                    Sequence(node.set(new Entries.Architecture()), SimpleEntry(node, "Architecture", ArchitectureIdentifier())),

                                    Sequence(node.set(new Entries.Maintainer()), SimpleEntry(node, "Maintainer")),
                                    Sequence(node.set(new Entries.Uploaders()), SimpleEntry(node, "Uploaders")),
                                    Sequence(node.set(new Entries.ChangedBy()), SimpleEntry(node, "Changed-By")),


                                    Sequence(node.set(new Entries.Section()), SimpleEntry(node, "Section")),
                                    Sequence(node.set(new Entries.Priority()), SimpleEntry(node, "Priority")),

                                    Sequence(node.set(new Entries.Essential()), SimpleEntry(node, "Essential")),

                                    Sequence(node.set(new Entries.StandardsVersion()), SimpleEntry(node, "Standards-Version")),

                                    Sequence(node.set(new Entries.Description()), MultilineEntry(node, "Description")),
                                    Sequence(node.set(new Entries.Distribution()), SimpleEntry(node, "Distribution")),
                                    Sequence(node.set(new Entries.Date()), SimpleEntry(node, "Date")),
                                    Sequence(node.set(new Entries.Format()), SimpleEntry(node, "Format")),
                                    Sequence(node.set(new Entries.Urgency()), SimpleEntry(node, "Urgency")),
                                    Sequence(node.set(new Entries.Changes()), SimpleEntry(node, "Changes")),
                                    Sequence(node.set(new Entries.Binary()), SimpleEntry(node, "Binary")),


                                    Sequence(node.set(new Entries.Files()), SimpleEntry(node, "Files")),
                                    Sequence(node.set(new Entries.InstalledSize()), SimpleEntry(node, "Installed-Size")),
                                    Sequence(node.set(new Entries.Size()), SimpleEntry(node, "Size")),
                                    Sequence(node.set(new Entries.MD5sum()), SimpleEntry(node, "MD5sum")),
                                    Sequence(node.set(new Entries.SHA1()), SimpleEntry(node, "SHA1")),
                                    Sequence(node.set(new Entries.SHA256()), SimpleEntry(node, "SHA256")),


                                    Sequence(node.set(new Entries.Filename()), SimpleEntry(node, "Filename")),
                                    Sequence(node.set(new Entries.Closes()), SimpleEntry(node, "Closes")),
                                    Sequence(node.set(new Entries.Homepage()), SimpleEntry(node, "Homepage")),

                                    Sequence(node.set(new Entries.ChecksumsSha1()), MultilineEntry(node, "Checksums-Sha1")),
                                    Sequence(node.set(new Entries.ChecksumsSha256()), MultilineEntry(node, "Checksums-Sha256")),
                                    Sequence(node.set(new Entries.DMUploadAllowed()), SimpleEntry(node, "DM-Upload-Allowed")),
                                    Sequence(node.set(new Entries.VcsBrowser()), SimpleEntry(node, "Vcs-Browser")),
                                    Sequence(node.set(new Entries.VcsArch()), SimpleEntry(node, "Vcs-Arch")),
                                    Sequence(node.set(new Entries.VcsBzr()), SimpleEntry(node, "Vcs-Bzr")),
                                    Sequence(node.set(new Entries.VcsCvs()), SimpleEntry(node, "Vcs-Cvs")),
                                    Sequence(node.set(new Entries.VcsDarcs()), SimpleEntry(node, "Vcs-Darcs")),
                                    Sequence(node.set(new Entries.VcsGit()), SimpleEntry(node, "Vcs-Git")),
                                    Sequence(node.set(new Entries.VcsHg()), SimpleEntry(node, "Vcs-Hg")),
                                    Sequence(node.set(new Entries.VcsMtn()), SimpleEntry(node, "Vcs-Mtn")),
                                    Sequence(node.set(new Entries.VcsSvn()), SimpleEntry(node, "Vcs-Svn")),
                                    Sequence(node.set(new Entries.Task()), SimpleEntry(node, "Task")),
                                    Sequence(node.set(new Entries.MetaPackage()), SimpleEntry(node, "meta-package")),
                                    Sequence(node.set(new Entries.PythonVersion()), SimpleEntry(node, "Python-Version")),
                                    Sequence(node.set(new Entries.PythonRuntime()), SimpleEntry(node, "Python-Runtime")),
                                    Sequence(node.set(new Entries.Bugs()), SimpleEntry(node, "Bugs")),
                                    Sequence(node.set(new Entries.Origin()), SimpleEntry(node, "Origin")),
                                    Sequence(node.set(new Entries.Url()), SimpleEntry(node, "Url")),
                                    Sequence(node.set(new Entries.Url()), SimpleEntry(node, "url")),
                                    Sequence(node.set(new Entries.Tag()), SimpleEntry(node, "Tag")),
                                    Sequence(node.set(new Entries.NppApplications()), MultilineEntry(node, "Npp-Applications")),
                                    Sequence(node.set(new Entries.NppDescription()), SimpleEntry(node, "Npp-Description")),
                                    Sequence(node.set(new Entries.NppFile()), SimpleEntry(node, "Npp-File")),
                                    Sequence(node.set(new Entries.NppMimetype()), MultilineEntry(node, "Npp-Mimetype")),
                                    Sequence(node.set(new Entries.NppName()), SimpleEntry(node, "Npp-Name")),
                                    Sequence(node.set(new Entries.Dummy()), MultilineEntry(node, "Original-Maintainer")),
                                    Sequence(node.set(new Entries.Dummy()), DummyEntry(node, OneOrMore(NoneOf("\n:")))) //TODO fixit???
                            ),
                            addAsChild("Entry")));
        }

    public Rule DummyEntry(@NotNull Var<SuperNode> node,Rule rule) {
            return Sequence(
                    push(node.get()),
                    rule,
                    WS(),
                    ":",
                    WS(),
                    RemainingLine());
    }

    public Action addAsChild() {
        return addAsChild("");
    }
    public Action addAsChild(String s) {

        return new Action() {
            @Override
            public boolean run(Context context) {
                SuperNode parent = (SuperNode) peek(1);
                Object o = pop();
                if(o == null)
                    throw new ParserRuntimeException("CHILD NULL");
                if(parent == null)
                    throw new ParserRuntimeException("PARENT NULL");
                if(o instanceof String)
                    parent.addChild(0, new StringNode((String) o));
                else
                    parent.addChild(0, (SuperNode)o);
                return true;
            }
        };
    }

    Rule DependencyList() {
        return Sequence(
                push(new DependencyConjunction()),
                DependencyTerm(),
                addAsChild("d1"),
                ZeroOrMore(
                        Sequence(
                                WS_Foldable(),
                                ',',
                                WS_Foldable(),
                                DependencyTerm(),
                                addAsChild("d2")
                        ))
        );
    }


    Rule DependencyTerm() {
        Var<SuperNode> node = new Var<SuperNode>(new DependencyDisjunction());
        return Sequence(
                push(node.get()),
                PackageIdentifier(),
                addAsChild("dis1"),
                        ZeroOrMore(
                                Sequence(
                                        WS_Foldable(),
                                        '|',
                                        WS_Foldable(),
                                        PackageIdentifier(),
                                        addAsChild("dis1")
                                )
                        )
                );
    }

    Rule ArchitectureRestriction() {
        return Sequence("[",WS(),Optional("!"),WS(),ArchitectureIdentifier(),WS(),"]"); //TODO
    }

    Rule ArchitectureIdentifier() {
        return Sequence(
                FirstOf("any",
                        "all",
                        "source",
                        Sequence(OSIdentifier(), "-", CPUIdentifier()),
                        OSIdentifier(),
                        CPUIdentifier()),
                push(match()));
    }

    Rule OSIdentifier() {
        return FirstOf("linux", "hurd","kfreebsd","netbsd");
    }
    Rule CPUIdentifier() {
        return FirstOf("amd64", "armel","armhf","i386","ia64","mipsel","mips","powerpc","s390x","s390","sparc","alpha","arm","AVR32","hppa","m32","m68k","sh");
    }

    Rule PackageIdentifier() {
        return Sequence(
                push(new PackageIdentifier()),
                PackageName(),
                addAsChild("name"),
                WS(),
                Optional(
                        VersionIdentifier(),
                        addAsChild("version")),
                Optional(ArchitectureRestriction(),
                        addAsChild("architecture"))
                );
    }

    @NotNull
    Action SetContextStringAction(@NotNull final StringVar stringVar) {
        return new Action(){
            @Override
            public boolean run(@NotNull Context context) {
                if(inPredicate())
                    return true;
                stringVar.set(context.getMatch());
                return true;
            }
        };
    }

    @SuppressSubnodes
     Rule PackageName() {
        return
                FirstOf(
                Sequence(

                        Sequence(LowerAlphaNumeric(), OneOrMore(FirstOf(LowerAlphaNumeric(), AnyOf("+-.")))),
                        push(match())
                ),
                        /* WARNING */
                        Sequence(

                                Sequence(AlphaNumeric(), OneOrMore(FirstOf(AlphaNumeric(), AnyOf("+-.")))),
                                push(match()),
                                new Action() {
                                    @Override
                                    public boolean run(Context context) {
                                        LOGGER.error("ATTENTION: UPPER CASE PACKAGE IDENTIIFER NAME IS ILLEGAL: '" + peek() + "'"); //TODO
                                        return true;
                                    }
                                }
                        )
                );
    }


    Rule VersionIdentifier(){
        final StringVar relation = new StringVar();
        final StringVar epoch = new StringVar();
        final StringVar upstreamVersion = new StringVar();
        final StringVar debVersion = new StringVar();
            return Sequence(
                    Optional("("),
                    WS(),
                    Optional(
                            VersionRelation(),
                            relation.set(pop().toString())),
                    Optional(
                            VersionEpoch(),
                            epoch.set(pop().toString())),
                    VersionUpstreamVersion(),
                    upstreamVersion.set((String)pop()),
                    Optional(
                            VersionDebianVersion(),
                            debVersion.set((String)pop())),
                    WS(),
                    Optional(")"),
                    WS(),
                    new Action() {
                        @Override
                        public boolean run(Context context) {
                            if(inPredicate())
                                return true;
                            Version version = new Version();
                            version.epoch = epoch.get();
                            version.upstreamVersion = upstreamVersion.get();
                            version.debversion = debVersion.get();
                            version.relation = relation.get();
                            push(version);
                            return true;
                        }
                    });
        }

    @SuppressSubnodes
    Rule VersionDebianVersion() {
        return Sequence('-',
                OneOrMore(FirstOf(AlphaNumeric(),'~','.','+')),
                push(match())
        );
    }
    

    @SuppressSubnodes
    Rule VersionUpstreamVersion() {
        return Sequence(
                OneOrMore(FirstOf(AlphaNumeric(), '~', '.', '+'/* NOT ALWAYS ALLOWED,'-',':'*/)),
                push(match())
        );
    }

    @SuppressSubnodes
    Rule VersionEpoch() {
        StringVar temp = new StringVar();
        return Sequence(
                Number(),
                SetContextStringAction(temp),
                ":",
                push(temp.get())
        );
    }

    Rule Figure(){
        return CharRange('0', '9');
    }
    Rule Number() {
        return OneOrMore(Figure());
    }

    Rule AlphaNumeric(){
        return FirstOf(CharRange('0', '9'),CharRange('a','z'),CharRange('A','Z'));
    }

    Rule LowerAlphaNumeric(){
        return FirstOf(CharRange('0', '9'),CharRange('a','z'));
    }

    @SuppressNode
    Rule WS() {
        return ZeroOrMore(AnyOf(" \t"));
    }

    @SuppressNode
    Rule WS_Foldable() {
        return ZeroOrMore(FirstOf(AnyOf(" \t"),"\n "));
    }

    @SuppressSubnodes
    Rule VersionRelation() {
        return Sequence(
                FirstOf("=", "<<", "<=", ">=", ">>", ">"/*DEPRECATED*/, "<"/*DEPRECATED*/),
                push(match()),
                WS());
    }
    Rule PackageEnd() {
        return FirstOf(
                OneOrMore("\n"),
                Test(BaseParser.EOI),
                Sequence(Test("Package: "), new Action() {
                    @Override
                    public boolean run(Context context) {
                        LOGGER.error("ATTENTION: NO EMPTY LINE BETWEEN PACKAGES"); //TODO
                        return true;
                    }
                }));
    }

    Rule SimpleEntry(@NotNull Var<SuperNode> node, String identifier) {
        return Sequence(
                push(node.get()),
                identifier,
                WS(),
                ":",
                WS(),
                RemainingLine(),
                push(match().trim()),
                addAsChild());
    }

    Rule SimpleEntry(@NotNull Var<SuperNode> node, @NotNull Object identifier, Rule o){
        return Sequence(
                push(node.get()),
                identifier,
                WS(),
                ":",
                WS(),
                o,
                addAsChild(identifier.toString()),
                RemainingLine());
    }

    Rule MultilineEntry(@NotNull Var<SuperNode> node, Object identifier){
        return Sequence(
                push(node.get()),
                identifier,
                WS(),
                ":",
                WS(),
                FirstOf(Line(), BuggyLine()),
                push(match()),
                addAsChild()
        );
    }

    @SuppressSubnodes
    Rule BuggyLine() {
        return Sequence(OneOrMore(FirstOf(Sequence("\n ",NoneOf(".")),"\n\t", NoneOf("\n"))),"\n .",   new Action() {
            @Override
            public boolean run(Context context) {
                LOGGER.error("ATTENTION: BUGGY LINE"); //TODO
                return true;
            }
        });
    }

    @SuppressSubnodes
    Rule RemainingLine() {
        return Sequence(ZeroOrMore(NoneOf("\n")),"\n");
    }
    @SuppressSubnodes
    Rule Line() {
        return Sequence(OneOrMore(FirstOf("\n ","\n\t", NoneOf("\n"))),"\n");
    }


}
