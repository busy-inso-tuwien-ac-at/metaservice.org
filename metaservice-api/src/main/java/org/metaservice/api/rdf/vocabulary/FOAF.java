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
 * @see <a href="http://xmlns.com/foaf/0.1/">foaf</a>
 */
public class FOAF{

    public static final String NAMESPACE = "http://xmlns.com/foaf/0.1/";

    public static final String PREFIX = "foaf";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://xmlns.com/foaf/0.1/Agent<br>
     * "Agent"<br>
     * An agent (eg. person, group, software or physical artifact).<br>
     */
    public static final URI AGENT;


    /**
     * http://xmlns.com/foaf/0.1/OnlineAccount<br>
     * "Online Account"<br>
     * An online account.<br>
     */
    public static final URI ONLINE_ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/OnlineGamingAccount<br>
     * "Online Gaming Account"<br>
     * An online gaming account.<br>
     */
    public static final URI ONLINE_GAMING_ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/Person<br>
     * "Person"<br>
     * A person.<br>
     */
    public static final URI PERSON;


    /**
     * http://xmlns.com/foaf/0.1/Organization<br>
     * "Organization"<br>
     * An organization.<br>
     */
    public static final URI ORGANIZATION;


    /**
     * http://xmlns.com/foaf/0.1/OnlineChatAccount<br>
     * "Online Chat Account"<br>
     * An online chat account.<br>
     */
    public static final URI ONLINE_CHAT_ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/Document<br>
     * "Document"<br>
     * A document.<br>
     */
    public static final URI DOCUMENT;


    /**
     * http://xmlns.com/foaf/0.1/Image<br>
     * "Image"<br>
     * An image.<br>
     */
    public static final URI IMAGE;


    /**
     * http://xmlns.com/foaf/0.1/LabelProperty<br>
     * "Label Property"<br>
     * A foaf:LabelProperty is any RDF property with texual values that serve as labels.<br>
     */
    public static final URI LABEL_PROPERTY;


    /**
     * http://xmlns.com/foaf/0.1/OnlineEcommerceAccount<br>
     * "Online E-commerce Account"<br>
     * An online e-commerce account.<br>
     */
    public static final URI ONLINE_ECOMMERCE_ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/Group<br>
     * "Group"<br>
     * A class of Agents.<br>
     */
    public static final URI GROUP;


    /**
     * http://xmlns.com/foaf/0.1/Project<br>
     * "Project"<br>
     * A project (a collective endeavour of some kind).<br>
     */
    public static final URI PROJECT;


    /**
     * http://xmlns.com/foaf/0.1/PersonalProfileDocument<br>
     * "PersonalProfileDocument"<br>
     * A personal profile RDF document.<br>
     */
    public static final URI PERSONAL_PROFILE_DOCUMENT;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://xmlns.com/foaf/0.1/tipjar<br>
     * "tipjar"<br>
     * A tipjar document for this agent, describing means for payment and reward.<br>
     */
    public static final URI TIPJAR;


    /**
     * http://xmlns.com/foaf/0.1/workplaceHomepage<br>
     * "workplace homepage"<br>
     * A workplace homepage of some person; the homepage of an organization they work for.<br>
     */
    public static final URI WORKPLACE_HOMEPAGE;


    /**
     * http://xmlns.com/foaf/0.1/maker<br>
     * "maker"<br>
     * An agent that  made this thing.<br>
     */
    public static final URI MAKER;


    /**
     * http://xmlns.com/foaf/0.1/based_near<br>
     * "based near"<br>
     * A location that something is based near, for some broadly human notion of near.<br>
     */
    public static final URI BASED_NEAR;


    /**
     * http://xmlns.com/foaf/0.1/depiction<br>
     * "depiction"<br>
     * A depiction of some thing.<br>
     */
    public static final URI DEPICTION;


    /**
     * http://xmlns.com/foaf/0.1/accountServiceHomepage<br>
     * "account service homepage"<br>
     * Indicates a homepage of the service provide for this online account.<br>
     */
    public static final URI ACCOUNT_SERVICE_HOMEPAGE;


    /**
     * http://xmlns.com/foaf/0.1/primaryTopic<br>
     * "primary topic"<br>
     * The primary topic of some page or document.<br>
     */
    public static final URI PRIMARY_TOPIC;


    /**
     * http://xmlns.com/foaf/0.1/logo<br>
     * "logo"<br>
     * A logo representing some thing.<br>
     */
    public static final URI LOGO;


    /**
     * http://xmlns.com/foaf/0.1/theme<br>
     * "theme"<br>
     * A theme.<br>
     */
    public static final URI THEME;


    /**
     * http://xmlns.com/foaf/0.1/page<br>
     * "page"<br>
     * A page or document about this thing.<br>
     */
    public static final URI PAGE;


    /**
     * http://xmlns.com/foaf/0.1/made<br>
     * "made"<br>
     * Something that was made by this agent.<br>
     */
    public static final URI MADE;


    /**
     * http://xmlns.com/foaf/0.1/mbox<br>
     * "personal mailbox"<br>
     * A  personal mailbox, ie. an Internet mailbox associated with exactly one owner, the first owner of this mailbox. This is a 'static inverse functional property', in that  there is (across time and change) at most one individual that ever has any particular value for foaf:mbox.<br>
     */
    public static final URI MBOX;


    /**
     * http://xmlns.com/foaf/0.1/topic<br>
     * "topic"<br>
     * A topic of some page or document.<br>
     */
    public static final URI TOPIC;


    /**
     * http://xmlns.com/foaf/0.1/holdsAccount<br>
     * "account"<br>
     * Indicates an account held by this agent.<br>
     */
    public static final URI HOLDS_ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/workInfoHomepage<br>
     * "work info homepage"<br>
     * A work info homepage of some person; a page about their work for some organization.<br>
     */
    public static final URI WORK_INFO_HOMEPAGE;


    /**
     * http://xmlns.com/foaf/0.1/homepage<br>
     * "homepage"<br>
     * A homepage for some thing.<br>
     */
    public static final URI HOMEPAGE;


    /**
     * http://xmlns.com/foaf/0.1/pastProject<br>
     * "past project"<br>
     * A project this person has previously worked on.<br>
     */
    public static final URI PAST_PROJECT;


    /**
     * http://xmlns.com/foaf/0.1/currentProject<br>
     * "current project"<br>
     * A current project this person works on.<br>
     */
    public static final URI CURRENT_PROJECT;


    /**
     * http://xmlns.com/foaf/0.1/thumbnail<br>
     * "thumbnail"<br>
     * A derived thumbnail image.<br>
     */
    public static final URI THUMBNAIL;


    /**
     * http://xmlns.com/foaf/0.1/topic_interest<br>
     * "topic_interest"<br>
     * A thing of interest to this person.<br>
     */
    public static final URI TOPIC_INTEREST;


    /**
     * http://xmlns.com/foaf/0.1/focus<br>
     * "focus"<br>
     * The underlying or 'focal' entity associated with some SKOS-described concept.<br>
     */
    public static final URI FOCUS;


    /**
     * http://xmlns.com/foaf/0.1/depicts<br>
     * "depicts"<br>
     * A thing depicted in this representation.<br>
     */
    public static final URI DEPICTS;


    /**
     * http://xmlns.com/foaf/0.1/weblog<br>
     * "weblog"<br>
     * A weblog of some thing (whether person, group, company etc.).<br>
     */
    public static final URI WEBLOG;


    /**
     * http://xmlns.com/foaf/0.1/knows<br>
     * "knows"<br>
     * A person known by this person (indicating some level of reciprocated interaction between the parties).<br>
     */
    public static final URI KNOWS;


    /**
     * http://xmlns.com/foaf/0.1/phone<br>
     * "phone"<br>
     * A phone,  specified using fully qualified tel: URI scheme (refs: http://www.w3.org/Addressing/schemes.html#tel).<br>
     */
    public static final URI PHONE;


    /**
     * http://xmlns.com/foaf/0.1/account<br>
     * "account"<br>
     * Indicates an account held by this agent.<br>
     */
    public static final URI ACCOUNT;


    /**
     * http://xmlns.com/foaf/0.1/fundedBy<br>
     * "funded by"<br>
     * An organization funding a project or person.<br>
     */
    public static final URI FUNDED_BY;


    /**
     * http://xmlns.com/foaf/0.1/publications<br>
     * "publications"<br>
     * A link to the publications of this person.<br>
     */
    public static final URI PUBLICATIONS;


    /**
     * http://xmlns.com/foaf/0.1/img<br>
     * "image"<br>
     * An image that can be used to represent some thing (ie. those depictions which are particularly representative of something, eg. one's photo on a homepage).<br>
     */
    public static final URI IMG;


    /**
     * http://xmlns.com/foaf/0.1/interest<br>
     * "interest"<br>
     * A page about a topic of interest to this person.<br>
     */
    public static final URI INTEREST;


    /**
     * http://xmlns.com/foaf/0.1/openid<br>
     * "openid"<br>
     * An OpenID for an Agent.<br>
     */
    public static final URI OPENID;


    /**
     * http://xmlns.com/foaf/0.1/member<br>
     * "member"<br>
     * Indicates a member of a Group<br>
     */
    public static final URI MEMBER;


    /**
     * http://xmlns.com/foaf/0.1/schoolHomepage<br>
     * "schoolHomepage"<br>
     * A homepage of a school attended by the person.<br>
     */
    public static final URI SCHOOL_HOMEPAGE;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://xmlns.com/foaf/0.1/msnChatID<br>
     * "MSN chat ID"<br>
     * An MSN chat ID<br>
     */
    public static final URI MSN_CHAT_ID;


    /**
     * http://xmlns.com/foaf/0.1/family_name<br>
     * "family_name"<br>
     * The family name of some person.<br>
     */
    public static final URI FAMILY_NAME;


    /**
     * http://xmlns.com/foaf/0.1/skypeID<br>
     * "Skype ID"<br>
     * A Skype ID<br>
     */
    public static final URI SKYPE_ID;


    /**
     * http://xmlns.com/foaf/0.1/givenName<br>
     * "Given name"<br>
     * The given name of some person.<br>
     */
    public static final URI GIVEN_NAME;


    /**
     * http://xmlns.com/foaf/0.1/age<br>
     * "age"<br>
     * The age in years of some agent.<br>
     */
    public static final URI AGE;


    /**
     * http://xmlns.com/foaf/0.1/birthday<br>
     * "birthday"<br>
     * The birthday of this Agent, represented in mm-dd string form, eg. '12-31'.<br>
     */
    public static final URI BIRTHDAY;


    /**
     * http://xmlns.com/foaf/0.1/lastName<br>
     * "lastName"<br>
     * The last name of a person.<br>
     */
    public static final URI LAST_NAME;


    /**
     * http://xmlns.com/foaf/0.1/familyName<br>
     * "familyName"<br>
     * The family name of some person.<br>
     */
    public static final URI FAMILY_NAME_PROPERTY;


    /**
     * http://xmlns.com/foaf/0.1/status<br>
     * "status"<br>
     * A string expressing what the user is happy for the general public (normally) to know about their current activity.<br>
     */
    public static final URI STATUS;


    /**
     * http://xmlns.com/foaf/0.1/yahooChatID<br>
     * "Yahoo chat ID"<br>
     * A Yahoo chat ID<br>
     */
    public static final URI YAHOO_CHAT_ID;


    /**
     * http://xmlns.com/foaf/0.1/firstName<br>
     * "firstName"<br>
     * The first name of a person.<br>
     */
    public static final URI FIRST_NAME;


    /**
     * http://xmlns.com/foaf/0.1/title<br>
     * "title"<br>
     * Title (Mr, Mrs, Ms, Dr. etc)<br>
     */
    public static final URI TITLE;


    /**
     * http://xmlns.com/foaf/0.1/aimChatID<br>
     * "AIM chat ID"<br>
     * An AIM chat ID<br>
     */
    public static final URI AIM_CHAT_ID;


    /**
     * http://xmlns.com/foaf/0.1/dnaChecksum<br>
     * "DNA checksum"<br>
     * A checksum for the DNA of some thing. Joke.<br>
     */
    public static final URI DNA_CHECKSUM;


    /**
     * http://xmlns.com/foaf/0.1/nick<br>
     * "nickname"<br>
     * A short informal nickname characterising an agent (includes login identifiers, IRC and other chat nicknames).<br>
     */
    public static final URI NICK;


    /**
     * http://xmlns.com/foaf/0.1/sha1<br>
     * "sha1sum (hex)"<br>
     * A sha1sum hash, in hex.<br>
     */
    public static final URI SHA1;


    /**
     * http://xmlns.com/foaf/0.1/plan<br>
     * "plan"<br>
     * A .plan comment, in the tradition of finger and '.plan' files.<br>
     */
    public static final URI PLAN;


    /**
     * http://xmlns.com/foaf/0.1/mbox_sha1sum<br>
     * "sha1sum of a personal mailbox URI name"<br>
     * The sha1sum of the URI of an Internet mailbox associated with exactly one owner, the  first owner of the mailbox.<br>
     */
    public static final URI MBOX_SHA1SUM;


    /**
     * http://xmlns.com/foaf/0.1/gender<br>
     * "gender"<br>
     * The gender of this Agent (typically but not necessarily 'male' or 'female').<br>
     */
    public static final URI GENDER;


    /**
     * http://xmlns.com/foaf/0.1/name<br>
     * "name"<br>
     * A name for some thing.<br>
     */
    public static final URI NAME;


    /**
     * http://xmlns.com/foaf/0.1/geekcode<br>
     * "geekcode"<br>
     * A textual geekcode for this person, see http://www.geekcode.com/geek.html<br>
     */
    public static final URI GEEKCODE;


    /**
     * http://xmlns.com/foaf/0.1/icqChatID<br>
     * "ICQ chat ID"<br>
     * An ICQ chat ID<br>
     */
    public static final URI ICQ_CHAT_ID;


    /**
     * http://xmlns.com/foaf/0.1/accountName<br>
     * "account name"<br>
     * Indicates the name (identifier) associated with this online account.<br>
     */
    public static final URI ACCOUNT_NAME;


    /**
     * http://xmlns.com/foaf/0.1/surname<br>
     * "Surname"<br>
     * The surname of some person.<br>
     */
    public static final URI SURNAME;


    /**
     * http://xmlns.com/foaf/0.1/givenname<br>
     * "Given name"<br>
     * The given name of some person.<br>
     */
    public static final URI GIVENNAME;


    /**
     * http://xmlns.com/foaf/0.1/myersBriggs<br>
     * "myersBriggs"<br>
     * A Myers Briggs (MBTI) personality classification.<br>
     */
    public static final URI MYERS_BRIGGS;


    /**
     * http://xmlns.com/foaf/0.1/jabberID<br>
     * "jabber ID"<br>
     * A jabber ID for something.<br>
     */
    public static final URI JABBER_ID;


////////////////////////
// ANNOTATION PROPERTIES
////////////////////////


    /**
     * http://xmlns.com/foaf/0.1/membershipClass<br>
     * "membershipClass"<br>
     * Indicates the class of individuals that are a member of a Group<br>
     */
    public static final URI MEMBERSHIP_CLASS;


////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://xmlns.com/foaf/0.1/isPrimaryTopicOf<br>
     * "is primary topic of"<br>
     * A document that this thing is the primary topic of.<br>
     */
    public static final URI IS_PRIMARY_TOPIC_OF;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        AGENT = valueFactory.createURI(NAMESPACE,"Agent");
        ONLINE_ACCOUNT = valueFactory.createURI(NAMESPACE,"OnlineAccount");
        ONLINE_GAMING_ACCOUNT = valueFactory.createURI(NAMESPACE,"OnlineGamingAccount");
        PERSON = valueFactory.createURI(NAMESPACE,"Person");
        ORGANIZATION = valueFactory.createURI(NAMESPACE,"Organization");
        ONLINE_CHAT_ACCOUNT = valueFactory.createURI(NAMESPACE,"OnlineChatAccount");
        DOCUMENT = valueFactory.createURI(NAMESPACE,"Document");
        IMAGE = valueFactory.createURI(NAMESPACE,"Image");
        LABEL_PROPERTY = valueFactory.createURI(NAMESPACE,"LabelProperty");
        ONLINE_ECOMMERCE_ACCOUNT = valueFactory.createURI(NAMESPACE,"OnlineEcommerceAccount");
        GROUP = valueFactory.createURI(NAMESPACE,"Group");
        PROJECT = valueFactory.createURI(NAMESPACE,"Project");
        PERSONAL_PROFILE_DOCUMENT = valueFactory.createURI(NAMESPACE,"PersonalProfileDocument");
        TIPJAR = valueFactory.createURI(NAMESPACE,"tipjar");
        WORKPLACE_HOMEPAGE = valueFactory.createURI(NAMESPACE,"workplaceHomepage");
        MAKER = valueFactory.createURI(NAMESPACE,"maker");
        BASED_NEAR = valueFactory.createURI(NAMESPACE,"based_near");
        DEPICTION = valueFactory.createURI(NAMESPACE,"depiction");
        ACCOUNT_SERVICE_HOMEPAGE = valueFactory.createURI(NAMESPACE,"accountServiceHomepage");
        PRIMARY_TOPIC = valueFactory.createURI(NAMESPACE,"primaryTopic");
        LOGO = valueFactory.createURI(NAMESPACE,"logo");
        THEME = valueFactory.createURI(NAMESPACE,"theme");
        PAGE = valueFactory.createURI(NAMESPACE,"page");
        MADE = valueFactory.createURI(NAMESPACE,"made");
        MBOX = valueFactory.createURI(NAMESPACE,"mbox");
        TOPIC = valueFactory.createURI(NAMESPACE,"topic");
        HOLDS_ACCOUNT = valueFactory.createURI(NAMESPACE,"holdsAccount");
        WORK_INFO_HOMEPAGE = valueFactory.createURI(NAMESPACE,"workInfoHomepage");
        HOMEPAGE = valueFactory.createURI(NAMESPACE,"homepage");
        PAST_PROJECT = valueFactory.createURI(NAMESPACE,"pastProject");
        CURRENT_PROJECT = valueFactory.createURI(NAMESPACE,"currentProject");
        THUMBNAIL = valueFactory.createURI(NAMESPACE,"thumbnail");
        TOPIC_INTEREST = valueFactory.createURI(NAMESPACE,"topic_interest");
        FOCUS = valueFactory.createURI(NAMESPACE,"focus");
        DEPICTS = valueFactory.createURI(NAMESPACE,"depicts");
        WEBLOG = valueFactory.createURI(NAMESPACE,"weblog");
        KNOWS = valueFactory.createURI(NAMESPACE,"knows");
        PHONE = valueFactory.createURI(NAMESPACE,"phone");
        ACCOUNT = valueFactory.createURI(NAMESPACE,"account");
        FUNDED_BY = valueFactory.createURI(NAMESPACE,"fundedBy");
        PUBLICATIONS = valueFactory.createURI(NAMESPACE,"publications");
        IMG = valueFactory.createURI(NAMESPACE,"img");
        INTEREST = valueFactory.createURI(NAMESPACE,"interest");
        OPENID = valueFactory.createURI(NAMESPACE,"openid");
        MEMBER = valueFactory.createURI(NAMESPACE,"member");
        SCHOOL_HOMEPAGE = valueFactory.createURI(NAMESPACE,"schoolHomepage");
        MSN_CHAT_ID = valueFactory.createURI(NAMESPACE,"msnChatID");
        FAMILY_NAME = valueFactory.createURI(NAMESPACE,"family_name");
        SKYPE_ID = valueFactory.createURI(NAMESPACE,"skypeID");
        GIVEN_NAME = valueFactory.createURI(NAMESPACE,"givenName");
        AGE = valueFactory.createURI(NAMESPACE,"age");
        BIRTHDAY = valueFactory.createURI(NAMESPACE,"birthday");
        LAST_NAME = valueFactory.createURI(NAMESPACE,"lastName");
        FAMILY_NAME_PROPERTY = valueFactory.createURI(NAMESPACE,"familyName");
        STATUS = valueFactory.createURI(NAMESPACE,"status");
        YAHOO_CHAT_ID = valueFactory.createURI(NAMESPACE,"yahooChatID");
        FIRST_NAME = valueFactory.createURI(NAMESPACE,"firstName");
        TITLE = valueFactory.createURI(NAMESPACE,"title");
        AIM_CHAT_ID = valueFactory.createURI(NAMESPACE,"aimChatID");
        DNA_CHECKSUM = valueFactory.createURI(NAMESPACE,"dnaChecksum");
        NICK = valueFactory.createURI(NAMESPACE,"nick");
        SHA1 = valueFactory.createURI(NAMESPACE,"sha1");
        PLAN = valueFactory.createURI(NAMESPACE,"plan");
        MBOX_SHA1SUM = valueFactory.createURI(NAMESPACE,"mbox_sha1sum");
        GENDER = valueFactory.createURI(NAMESPACE,"gender");
        NAME = valueFactory.createURI(NAMESPACE,"name");
        GEEKCODE = valueFactory.createURI(NAMESPACE,"geekcode");
        ICQ_CHAT_ID = valueFactory.createURI(NAMESPACE,"icqChatID");
        ACCOUNT_NAME = valueFactory.createURI(NAMESPACE,"accountName");
        SURNAME = valueFactory.createURI(NAMESPACE,"surname");
        GIVENNAME = valueFactory.createURI(NAMESPACE,"givenname");
        MYERS_BRIGGS = valueFactory.createURI(NAMESPACE,"myersBriggs");
        JABBER_ID = valueFactory.createURI(NAMESPACE,"jabberID");
        MEMBERSHIP_CLASS = valueFactory.createURI(NAMESPACE,"membershipClass");
        IS_PRIMARY_TOPIC_OF = valueFactory.createURI(NAMESPACE,"isPrimaryTopicOf");
    }
}
