/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://www.w3.org/1999/xhtml/vocab#">xhv</a>
 */
public class XHV{

    public static final String NAMESPACE = "http://www.w3.org/1999/xhtml/vocab#";

    public static final String PREFIX = "xhv";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://www.w3.org/1999/xhtml/vocab#search<br>
     * indicates<br>
     *       that the section provides a search facility.<br>
     */
    public static final URI SEARCH;


    /**
     * http://www.w3.org/1999/xhtml/vocab#icon<br>
     * icon refers to a resource that<br>
     *       represents an icon.<br>
     */
    public static final URI ICON;


    /**
     * http://www.w3.org/1999/xhtml/vocab#banner<br>
     * contains<br>
     *       the prime heading or internal title of a page.<br>
     */
    public static final URI BANNER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#itsRules<br>
     * itsRules indicates that the designated<br>
     *       resource is an [ITS] rule set.<br>
     */
    public static final URI ITS_RULES;


    /**
     * http://www.w3.org/1999/xhtml/vocab#tabpanel<br>
     * A container<br>
     *       for the resources associated with a tab, where each tab is contained in<br>
     *       a tablist.<br>
     */
    public static final URI TABPANEL;


    /**
     * http://www.w3.org/1999/xhtml/vocab#tree<br>
     * A type of<br>
     *       list that may contain sub-level nested groups that can be collapsed and<br>
     *       expanded.<br>
     */
    public static final URI TREE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#treegrid<br>
     * A grid<br>
     *       whose rows can be expanded and collapsed in the same manner as for a tree.<br>
     */
    public static final URI TREEGRID;


    /**
     * http://www.w3.org/1999/xhtml/vocab#note<br>
     * indicates<br>
     *       the content is parenthetic or ancillary to the main content of the<br>
     *       resource.<br>
     */
    public static final URI NOTE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#contentinfo<br>
     * contains meta information about the<br>
     *       content on the page or the page as a whole.<br>
     */
    public static final URI CONTENTINFO;


    /**
     * http://www.w3.org/1999/xhtml/vocab#tab<br>
     * A grouping<br>
     *       label providing a mechanism for selecting the tab content that is to be<br>
     *       rendered to the user.<br>
     */
    public static final URI TAB;


    /**
     * http://www.w3.org/1999/xhtml/vocab#button<br>
     * An input<br>
     *       that allows for user-triggered actions when clicked or pressed. Also see<br>
     *       link.<br>
     */
    public static final URI BUTTON;


    /**
     * http://www.w3.org/1999/xhtml/vocab#prev<br>
     * prev refers to a previous resource<br>
     *       (before the current one) in an ordered collection of resources.<br>
     */
    public static final URI PREV;


    /**
     * http://www.w3.org/1999/xhtml/vocab#gridcell<br>
     * A cell<br>
     *       in a grid or treegrid.<br>
     */
    public static final URI GRIDCELL;


    /**
     * http://www.w3.org/1999/xhtml/vocab#chapter<br>
     * chapter refers to a resource serving<br>
     *       as a chapter in a collection.<br>
     */
    public static final URI CHAPTER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#separator<br>
     * A divider<br>
     *       that separates and distinguishes sections of content or groups of menuitems.<br>
     */
    public static final URI SEPARATOR;


    /**
     * http://www.w3.org/1999/xhtml/vocab#option<br>
     * A selectable<br>
     *       item in a select list.<br>
     */
    public static final URI OPTION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#p3pv1<br>
     * p3pv1 refers to a P3P Policy Reference<br>
     *       File [P3P].<br>
     */
    public static final URI P3PV1;


    /**
     * http://www.w3.org/1999/xhtml/vocab#img<br>
     * A container<br>
     *       for a collection of elements that form an image.<br>
     */
    public static final URI IMG;


    /**
     * http://www.w3.org/1999/xhtml/vocab#copyright<br>
     * copyright refers to a copyright<br>
     *       statement for the resource.<br>
     */
    public static final URI COPYRIGHT;


    /**
     * http://www.w3.org/1999/xhtml/vocab#menubar<br>
     * A presentation<br>
     *       of menu that usually remains visible and is usually presented horizontally.<br>
     */
    public static final URI MENUBAR;


    /**
     * http://www.w3.org/1999/xhtml/vocab#start<br>
     * start refers to the first resource in<br>
     *       a collection of resources.<br>
     */
    public static final URI START;


    /**
     * http://www.w3.org/1999/xhtml/vocab#checkbox<br>
     * A checkable<br>
     *       input that has three possible values: true, false, or mixed.<br>
     */
    public static final URI CHECKBOX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#columnheader<br>
     * A<br>
     *       cell containing header information for a column.<br>
     */
    public static final URI COLUMNHEADER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#last<br>
     * last refers to the last resource in a<br>
     *       collection of resources.<br>
     */
    public static final URI LAST;


    /**
     * http://www.w3.org/1999/xhtml/vocab#menuitem<br>
     * An option<br>
     *       in a group of choices contained by a menu or menubar.<br>
     */
    public static final URI MENUITEM;


    /**
     * http://www.w3.org/1999/xhtml/vocab#region<br>
     * A large<br>
     *       perceivable section of a web page or document, that the author feels is<br>
     *       important enough to be included in a page summary or table of contents,<br>
     *       for example, an area of the page containing live sporting event statistics.<br>
     */
    public static final URI REGION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#up<br>
     * up refers to a resource "above" in a<br>
     *       hierarchically structured set.<br>
     */
    public static final URI UP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#treeitem<br>
     * An option<br>
     *       item of a tree. This is an element within a tree that may be expanded or<br>
     *       collapsed if it contains a sub-level group of treeitems.<br>
     */
    public static final URI TREEITEM;


    /**
     * http://www.w3.org/1999/xhtml/vocab#slider<br>
     * A user<br>
     *       input where the user selects a value from within a given range.<br>
     */
    public static final URI SLIDER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#rowheader<br>
     * A cell<br>
     *       containing header information for a row in a grid.<br>
     */
    public static final URI ROWHEADER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#toolbar<br>
     * A collection<br>
     *       of commonly used function buttons represented in compact visual form.<br>
     */
    public static final URI TOOLBAR;


    /**
     * http://www.w3.org/1999/xhtml/vocab#heading<br>
     * A heading<br>
     *       for a section of the page.<br>
     */
    public static final URI HEADING;


    /**
     * http://www.w3.org/1999/xhtml/vocab#list<br>
     * A group of<br>
     *       non-interactive list items. Also see listbox.<br>
     */
    public static final URI LIST;


    /**
     * http://www.w3.org/1999/xhtml/vocab#top<br>
     * top is a synonym for start.<br>
     */
    public static final URI TOP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#contents<br>
     * contents refers to a resource serving<br>
     *       as a table of contents.<br>
     */
    public static final URI CONTENTS;


    /**
     * http://www.w3.org/1999/xhtml/vocab#timer<br>
     * A type of<br>
     *       live region containing a numerical counter which indicates an amount of<br>
     *       elapsed time from a start point, or the time remaining until an end point.<br>
     */
    public static final URI TIMER;


    /**
     * http://www.w3.org/1999/xhtml/vocab#menuitemcheckbox<br>
     * A<br>
     *       checkable menuitem that has three possible values: true, false, or mixed.<br>
     */
    public static final URI MENUITEMCHECKBOX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#first<br>
     * first refers the first item in a<br>
     *       collection (see also start and top).<br>
     */
    public static final URI FIRST;


    /**
     * http://www.w3.org/1999/xhtml/vocab#radio<br>
     * A checkable<br>
     *       input in a group of radio roles, only one of which can be checked at a<br>
     *       time.<br>
     */
    public static final URI RADIO;


    /**
     * http://www.w3.org/1999/xhtml/vocab#stylesheet<br>
     * stylesheet refers to a resource<br>
     *       serving as a stylesheet for a resource.<br>
     */
    public static final URI STYLESHEET;


    /**
     * http://www.w3.org/1999/xhtml/vocab#status<br>
     * A container<br>
     *       whose content is advisory information for the user but is not important<br>
     *       enough to justify an alert. Also see alert.<br>
     */
    public static final URI STATUS;


    /**
     * http://www.w3.org/1999/xhtml/vocab#role<br>
     * role indicates the purpose of the<br>
     *       resource. See the XHTML Role<br>
     *       Vocabulary for roles in this vocabulary space, and XHTMLROLE for information on extending the<br>
     *       collection of roles.<br>
     */
    public static final URI ROLE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#alert<br>
     * A message<br>
     *       with important, and usually time-sensitive, information. Also see alertdialog<br>
     *       and status.<br>
     */
    public static final URI ALERT;


    /**
     * http://www.w3.org/1999/xhtml/vocab#marquee<br>
     * A type<br>
     *       of live region where non-essential information changes frequently. Also<br>
     *       see log.<br>
     */
    public static final URI MARQUEE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#index<br>
     * index refers to a resource providing<br>
     *       an index.<br>
     */
    public static final URI INDEX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#combobox<br>
     * A presentation<br>
     *       of a select; usually similar to a textbox where users can type ahead to<br>
     *       select an option, or type to enter arbitrary text as a new item in the<br>
     *       list. Also see listbox.<br>
     */
    public static final URI COMBOBOX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#rowgroup<br>
     * A group<br>
     *       containing one or more row elements in a grid.<br>
     */
    public static final URI ROWGROUP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#help<br>
     * help refers to a resource offering<br>
     *       help.<br>
     */
    public static final URI HELP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#form<br>
     * A landmark<br>
     *       region that contains a collection of items and objects that, as a whole,<br>
     *       combine to create a form. Also see search.<br>
     */
    public static final URI FORM;


    /**
     * http://www.w3.org/1999/xhtml/vocab#definition<br>
     * indicates the definition of a term or concept.<br>
     */
    public static final URI DEFINITION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#glossary<br>
     * glossary refers to a resource<br>
     *       providing a glossary of terms.<br>
     */
    public static final URI GLOSSARY;


    /**
     * http://www.w3.org/1999/xhtml/vocab#subsection<br>
     * subsection refers to a resource<br>
     *       serving as a subsection in a collection.<br>
     */
    public static final URI SUBSECTION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#scrollbar<br>
     * A graphical<br>
     *       object that controls the scrolling of content within a viewing area, regardless<br>
     *       of whether the content is fully displayed within the viewing area.<br>
     */
    public static final URI SCROLLBAR;


    /**
     * http://www.w3.org/1999/xhtml/vocab#listitem<br>
     * A single<br>
     *       item in a list or directory.<br>
     */
    public static final URI LISTITEM;


    /**
     * http://www.w3.org/1999/xhtml/vocab#math<br>
     * Content that<br>
     *       represents a mathematical expression.<br>
     */
    public static final URI MATH;


    /**
     * http://www.w3.org/1999/xhtml/vocab#document<br>
     * A region<br>
     *       containing related information that is declared as document content, as<br>
     *       opposed to a web application.<br>
     */
    public static final URI DOCUMENT;


    /**
     * http://www.w3.org/1999/xhtml/vocab#row<br>
     * A row of cells<br>
     *       in a grid.<br>
     */
    public static final URI ROW;


    /**
     * http://www.w3.org/1999/xhtml/vocab#log<br>
     * A type of<br>
     *       live region where new information is added in meaningful order and old<br>
     *       information may disappear. Also see marquee.<br>
     */
    public static final URI LOG;


    /**
     * http://www.w3.org/1999/xhtml/vocab#alertdialog<br>
     * A<br>
     *       type of dialog that contains an alert message, where initial focus goes<br>
     *       an element within the dialog. Also see alert and dialog.<br>
     */
    public static final URI ALERTDIALOG;


    /**
     * http://www.w3.org/1999/xhtml/vocab#dialog<br>
     * A dialog<br>
     *       is an application window that is designed to interrupt the current processing<br>
     *       of an application in order to prompt the user to enter information or require<br>
     *       a response. Also see alertdialog.<br>
     */
    public static final URI DIALOG;


    /**
     * http://www.w3.org/1999/xhtml/vocab#navigation<br>
     * indicates a collection of items suitable for<br>
     *       navigating the document or related documents.<br>
     */
    public static final URI NAVIGATION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#meta<br>
     * meta refers to a resource that<br>
     *       provides metadata.<br>
     */
    public static final URI META;


    /**
     * http://www.w3.org/1999/xhtml/vocab#presentation<br>
     * An<br>
     *       element whose implicit native role semantics will not be mapped to the<br>
     *       accessibility API.<br>
     */
    public static final URI PRESENTATION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#textbox<br>
     * Input<br>
     *       that allows free-form text as its value.<br>
     */
    public static final URI TEXTBOX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#spinbutton<br>
     * A form<br>
     *       of range that expects a user to select from amongst discrete choices.<br>
     */
    public static final URI SPINBUTTON;


    /**
     * http://www.w3.org/1999/xhtml/vocab#tooltip<br>
     * A contextual<br>
     *       popup that displays a description for an element.<br>
     */
    public static final URI TOOLTIP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#license<br>
     * license refers to a resource that<br>
     *       defines the associated license.<br>
     */
    public static final URI LICENSE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#tablist<br>
     * A list<br>
     *       of tab elements, which are references to tabpanel elements.<br>
     */
    public static final URI TABLIST;


    /**
     * http://www.w3.org/1999/xhtml/vocab#section<br>
     * section refers to a resource serving<br>
     *       as a section in a collection.<br>
     */
    public static final URI SECTION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#next<br>
     * next refers to the next resource<br>
     *       (after the current one) in an ordered collection of resources.<br>
     */
    public static final URI NEXT;


    /**
     * http://www.w3.org/1999/xhtml/vocab#progressbar<br>
     * An<br>
     *       element that displays the progress status for tasks that take a long time.<br>
     */
    public static final URI PROGRESSBAR;


    /**
     * http://www.w3.org/1999/xhtml/vocab#application<br>
     * A<br>
     *       region declared as a web application, as opposed to a web document.<br>
     */
    public static final URI APPLICATION;


    /**
     * http://www.w3.org/1999/xhtml/vocab#radiogroup<br>
     * A group<br>
     *       of radio buttons.<br>
     */
    public static final URI RADIOGROUP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#cite<br>
     * cite refers to a resource that defines<br>
     *       a citation.<br>
     */
    public static final URI CITE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#group<br>
     * A set of<br>
     *       user interface objects which are not intended to be included in a page<br>
     *       summary or table of contents by assistive technologies.<br>
     */
    public static final URI GROUP;


    /**
     * http://www.w3.org/1999/xhtml/vocab#listbox<br>
     * A widget<br>
     *       that allows the user to select one or more items from a list of choices.<br>
     *       Also see combobox and list.<br>
     */
    public static final URI LISTBOX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#grid<br>
     * A grid is<br>
     *       an interactive control which contains cells of tabular data arranged in<br>
     *       rows and columns, like a table.<br>
     */
    public static final URI GRID;


    /**
     * http://www.w3.org/1999/xhtml/vocab#link<br>
     * An interactive<br>
     *       reference to an internal or external resource that, when activated, causes<br>
     *       the user agent to navigate to that resource. Also see button.<br>
     */
    public static final URI LINK;


    /**
     * http://www.w3.org/1999/xhtml/vocab#article<br>
     * A section<br>
     *       of a page that consists of a composition that forms an independent part<br>
     *       of a document, page, or site.<br>
     */
    public static final URI ARTICLE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#bookmark<br>
     * bookmark refers to a bookmark - a link<br>
     *       to a key entry point within an extended document.<br>
     */
    public static final URI BOOKMARK;


    /**
     * http://www.w3.org/1999/xhtml/vocab#directory<br>
     * A list<br>
     *       of references to members of a group, such as a static table of contents.<br>
     */
    public static final URI DIRECTORY;


    /**
     * http://www.w3.org/1999/xhtml/vocab#alternate<br>
     * alternate <br>
     *     designates alternate versions for a resource.<br>
     */
    public static final URI ALTERNATE;


    /**
     * http://www.w3.org/1999/xhtml/vocab#main<br>
     * acts as the<br>
     *       main content of the document.<br>
     */
    public static final URI MAIN;


    /**
     * http://www.w3.org/1999/xhtml/vocab#menu<br>
     * A type of<br>
     *       widget that offers a list of choices to the user.<br>
     */
    public static final URI MENU;


    /**
     * http://www.w3.org/1999/xhtml/vocab#menuitemradio<br>
     * A<br>
     *       checkable menuitem in a group of menuitemradio roles, only one of which<br>
     *       can be checked at a time.<br>
     */
    public static final URI MENUITEMRADIO;


    /**
     * http://www.w3.org/1999/xhtml/vocab#appendix<br>
     * appendix refers to a resource serving<br>
     *       as an appendix in a collection.<br>
     */
    public static final URI APPENDIX;


    /**
     * http://www.w3.org/1999/xhtml/vocab#complementary<br>
     * indicates that the section supports but is<br>
     *       separable from the main content of resource.<br>
     */
    public static final URI COMPLEMENTARY;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SEARCH = valueFactory.createURI(NAMESPACE,"search");
        ICON = valueFactory.createURI(NAMESPACE,"icon");
        BANNER = valueFactory.createURI(NAMESPACE,"banner");
        ITS_RULES = valueFactory.createURI(NAMESPACE,"itsRules");
        TABPANEL = valueFactory.createURI(NAMESPACE,"tabpanel");
        TREE = valueFactory.createURI(NAMESPACE,"tree");
        TREEGRID = valueFactory.createURI(NAMESPACE,"treegrid");
        NOTE = valueFactory.createURI(NAMESPACE,"note");
        CONTENTINFO = valueFactory.createURI(NAMESPACE,"contentinfo");
        TAB = valueFactory.createURI(NAMESPACE,"tab");
        BUTTON = valueFactory.createURI(NAMESPACE,"button");
        PREV = valueFactory.createURI(NAMESPACE,"prev");
        GRIDCELL = valueFactory.createURI(NAMESPACE,"gridcell");
        CHAPTER = valueFactory.createURI(NAMESPACE,"chapter");
        SEPARATOR = valueFactory.createURI(NAMESPACE,"separator");
        OPTION = valueFactory.createURI(NAMESPACE,"option");
        P3PV1 = valueFactory.createURI(NAMESPACE,"p3pv1");
        IMG = valueFactory.createURI(NAMESPACE,"img");
        COPYRIGHT = valueFactory.createURI(NAMESPACE,"copyright");
        MENUBAR = valueFactory.createURI(NAMESPACE,"menubar");
        START = valueFactory.createURI(NAMESPACE,"start");
        CHECKBOX = valueFactory.createURI(NAMESPACE,"checkbox");
        COLUMNHEADER = valueFactory.createURI(NAMESPACE,"columnheader");
        LAST = valueFactory.createURI(NAMESPACE,"last");
        MENUITEM = valueFactory.createURI(NAMESPACE,"menuitem");
        REGION = valueFactory.createURI(NAMESPACE,"region");
        UP = valueFactory.createURI(NAMESPACE,"up");
        TREEITEM = valueFactory.createURI(NAMESPACE,"treeitem");
        SLIDER = valueFactory.createURI(NAMESPACE,"slider");
        ROWHEADER = valueFactory.createURI(NAMESPACE,"rowheader");
        TOOLBAR = valueFactory.createURI(NAMESPACE,"toolbar");
        HEADING = valueFactory.createURI(NAMESPACE,"heading");
        LIST = valueFactory.createURI(NAMESPACE,"list");
        TOP = valueFactory.createURI(NAMESPACE,"top");
        CONTENTS = valueFactory.createURI(NAMESPACE,"contents");
        TIMER = valueFactory.createURI(NAMESPACE,"timer");
        MENUITEMCHECKBOX = valueFactory.createURI(NAMESPACE,"menuitemcheckbox");
        FIRST = valueFactory.createURI(NAMESPACE,"first");
        RADIO = valueFactory.createURI(NAMESPACE,"radio");
        STYLESHEET = valueFactory.createURI(NAMESPACE,"stylesheet");
        STATUS = valueFactory.createURI(NAMESPACE,"status");
        ROLE = valueFactory.createURI(NAMESPACE,"role");
        ALERT = valueFactory.createURI(NAMESPACE,"alert");
        MARQUEE = valueFactory.createURI(NAMESPACE,"marquee");
        INDEX = valueFactory.createURI(NAMESPACE,"index");
        COMBOBOX = valueFactory.createURI(NAMESPACE,"combobox");
        ROWGROUP = valueFactory.createURI(NAMESPACE,"rowgroup");
        HELP = valueFactory.createURI(NAMESPACE,"help");
        FORM = valueFactory.createURI(NAMESPACE,"form");
        DEFINITION = valueFactory.createURI(NAMESPACE,"definition");
        GLOSSARY = valueFactory.createURI(NAMESPACE,"glossary");
        SUBSECTION = valueFactory.createURI(NAMESPACE,"subsection");
        SCROLLBAR = valueFactory.createURI(NAMESPACE,"scrollbar");
        LISTITEM = valueFactory.createURI(NAMESPACE,"listitem");
        MATH = valueFactory.createURI(NAMESPACE,"math");
        DOCUMENT = valueFactory.createURI(NAMESPACE,"document");
        ROW = valueFactory.createURI(NAMESPACE,"row");
        LOG = valueFactory.createURI(NAMESPACE,"log");
        ALERTDIALOG = valueFactory.createURI(NAMESPACE,"alertdialog");
        DIALOG = valueFactory.createURI(NAMESPACE,"dialog");
        NAVIGATION = valueFactory.createURI(NAMESPACE,"navigation");
        META = valueFactory.createURI(NAMESPACE,"meta");
        PRESENTATION = valueFactory.createURI(NAMESPACE,"presentation");
        TEXTBOX = valueFactory.createURI(NAMESPACE,"textbox");
        SPINBUTTON = valueFactory.createURI(NAMESPACE,"spinbutton");
        TOOLTIP = valueFactory.createURI(NAMESPACE,"tooltip");
        LICENSE = valueFactory.createURI(NAMESPACE,"license");
        TABLIST = valueFactory.createURI(NAMESPACE,"tablist");
        SECTION = valueFactory.createURI(NAMESPACE,"section");
        NEXT = valueFactory.createURI(NAMESPACE,"next");
        PROGRESSBAR = valueFactory.createURI(NAMESPACE,"progressbar");
        APPLICATION = valueFactory.createURI(NAMESPACE,"application");
        RADIOGROUP = valueFactory.createURI(NAMESPACE,"radiogroup");
        CITE = valueFactory.createURI(NAMESPACE,"cite");
        GROUP = valueFactory.createURI(NAMESPACE,"group");
        LISTBOX = valueFactory.createURI(NAMESPACE,"listbox");
        GRID = valueFactory.createURI(NAMESPACE,"grid");
        LINK = valueFactory.createURI(NAMESPACE,"link");
        ARTICLE = valueFactory.createURI(NAMESPACE,"article");
        BOOKMARK = valueFactory.createURI(NAMESPACE,"bookmark");
        DIRECTORY = valueFactory.createURI(NAMESPACE,"directory");
        ALTERNATE = valueFactory.createURI(NAMESPACE,"alternate");
        MAIN = valueFactory.createURI(NAMESPACE,"main");
        MENU = valueFactory.createURI(NAMESPACE,"menu");
        MENUITEMRADIO = valueFactory.createURI(NAMESPACE,"menuitemradio");
        APPENDIX = valueFactory.createURI(NAMESPACE,"appendix");
        COMPLEMENTARY = valueFactory.createURI(NAMESPACE,"complementary");
    }
}
