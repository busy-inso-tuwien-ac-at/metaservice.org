# metaservice

Software dependencies and other interrelationships have quite an impact on Software Engineering and Maintenance.
Software repositories like Maven central, Debian or npm provide excellent information on interrelationships, but are still bound to a single ecosystem.

**metaservice retrieves software interrelationship information from these repositories and provides a semantically consistent interface**.

## About metaservice
metaservice is a prototype and has not reached production readiness.

A comprehensive overview over the concepts and structure of metaservice can be found in the linked master thesis below.

## Contribute

The code is made available undder the [Apache 2.0 License](./LICENSE). Feel free to use/fork/contribute. It would be nice to hear from you if you do so.

Although I haven't had time to further push this project,  I'm still  interested in the project and/or general topic.
Feel free to contact me (the author) under:
[nikola.ilo@gmail.com](mailto:nikola.ilo@gmail.com)

The [research group for Industrial Software of the TU Vienna](https://www.inso.tuwien.ac.at/) has expressed interest in further development of the research topic and this prototype.


## Acknowledgments
I want to thank [Mario Bernhart](https://www.inso.tuwien.ac.at/people/directing-competence/mario-bernhart/), who allowed me to explore my master thesis topic freely and thereby develop the code in this repository.    
A special thank also goes to Domink Moser, who looked through licensing issues with the source code and consistently reminded me to make this repository public.

## Scientific Publications
######Abstract

> Software interrelationships have an impact on the quality and evolution of software projects and are therefore important to development and maintenance. Package management and build systems result in software ecosystems that usually are syntactically and semantically incompatible with each other, although the described software can overlap. There is currently no general way for querying software interrelationships across these different ecosystems. In this paper, we present our approach to combine and consequently query information about software interrelationships across different ecosystems. We propose an ontology for the semantic modeling of the relationships as linked data. Furthermore, we introduce a temporal storage and query model to handle inconsistencies between different data sources. By providing a scalable and extensible architecture to retrieve and process data from multiple repositories, we establish a foundation for ongoing research activities. We evaluated our approach by integrating the data of several ecosystems and demonstrated its usefulness by creating tools for vulnerability notification and license violation detection.

### Master Thesis

Nikola Ilo. *Design and Development of a Service for Software Interrelationships*. 2014 Vienna University of Technology
[Full PDF (153 Pages)](http://repositum.tuwien.ac.at/obvutwhs/download/pdf/1634167?originalFilename=true)

### ICSME 2015
Nikola Ilo, Johann Grabner, Thomas Artner, Mario Bernhart, and Thomas Grechenig. *Combining software interrelationship data across heterogeneous software repositories.* In Software Maintenance and Evolution (ICSME), 2015 IEEE International Conference on, pp. 571-575. IEEE, 2015 [IEEE Link](http://ieeexplore.ieee.org/document/7332516/)

