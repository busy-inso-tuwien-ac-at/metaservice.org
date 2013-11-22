package org.metaservice.core.deb.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PackageIdentifier extends SuperNode {

    @Nullable
    String name = null;
    @Nullable
    Version version = null;


    @Override
    public void addChild(int i, @NotNull SuperNode superNode) {
        super.addChild(i, superNode);

        if(superNode instanceof Version){
            version = (Version) superNode;
        }else if (superNode instanceof StringNode){
            name = superNode.toString();
        }
    }


    @Nullable
    @Override
    public String toString() {
        return name + ((version!=null)?" " +version:"");
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public Version getVersion() {
        return version;
    }
}
