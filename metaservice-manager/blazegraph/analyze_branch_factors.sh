#!/usr/bin/env bash
#THIS DOESN'T WORK for 1.5.2!!!!
curl -d 'dumpPages' -d 'dumpJournal' 'http://localhost:9999/bigdata/status' | grep -E "^.*\.lex.*\t|^.*\.spo.*\t" | \
awk '{if($33 >3 && $33 <=4096) {printf("com.bigdata.namespace.%s.com.bigdata.btree.BTree.branchingFactor=%d;\n",$1,$33)}}' > branch.properties