package nlp.datastructure;

import java.util.*;

public class DependencyTreeNode {
    private Word word;
    private String dependecyParentToChild = "";
    private DependencyTreeNode parent;
    private ArrayList<DependencyTreeNode> children;
    private int height = -1;

    private boolean getParentNounCondition() {
        return this.parent != null && (this.dependecyParentToChild.equals("nn") ||
                (this.getWord().getPosTag().startsWith("NN") &&
                        this.dependecyParentToChild.equals("dep")));
    }

    private boolean getLinkNounCondition() {
        return this.parent != null && (this.dependecyParentToChild.equals("nn") ||
                (this.getWord().getPosTag().startsWith("NN") &&
                        this.dependecyParentToChild.equals("dep") &&
                        this.parent.getWord().getPosTag().startsWith("NN")));
    }

    public DependencyTreeNode(Word word,
                              String dependecyParentToChild,
                              DependencyTreeNode parent) {
        this.word = word;
        this.dependecyParentToChild = dependecyParentToChild;
        this.parent = parent;
        this.children = new ArrayList<>();

        height = parent == null ? 0 : parent.height + 1;
    }

    public DependencyTreeNode(Word word) {
        this.word = word;
        this.children = new ArrayList<>();
    }

    public Word getWord() { return this.word; }

    /**
     * Search in bottom-up fashion, find the maximum subtree rooted by a noun.
     *
     * @return : Found maximum subtree rooted by a noun in current dependency tree
     */
    public DependencyTreeNode getNounRootTopNode() {
        if (this.getParentNounCondition()) {
            return this.parent.getNounRootTopNode();
        } else {
            return this;
        }
    }
    public String getDependecyParentToChild() { return this.dependecyParentToChild; }
    public DependencyTreeNode getParent() { return this.parent; }
    public ArrayList<DependencyTreeNode> getChildren() { return this.children; }
    public int getHeight() { return this.height; }

    public void setDependecyParentToChild(String dependency) { this.dependecyParentToChild = dependency; }
    public void setParent(DependencyTreeNode parent) { this.parent = parent; }
    public void setHeight(int height) { this.height = height; }

    @Override
    public int hashCode() {
        return this.word.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof DependencyTreeNode) {
            return this.word.equals(((DependencyTreeNode) that).word);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return word.getOriginalForm() + "-" + word.getPosTag() + "(" + dependecyParentToChild + ")[" + word.getPosition() + "]";

//        return this.getWord().toString();
    }

    public void sortChildren() {
        children.trimToSize();
        children.sort(Comparator.comparingInt(node -> node.getWord().getPosition()));
    }

    public DependencyTreeNode getOverlappedChildrenDependency(String dependency) {
        for (DependencyTreeNode child: this.children) {
            if (child.dependecyParentToChild.equals(dependency)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Link the previous and next NN to current NN
     *
     */
    public Word linkNoun() {
        List<DependencyTreeNode> nouns = new ArrayList<>();
        nouns.add(this);

        if (this.getLinkNounCondition()) {
            nouns.add(this.parent);
            for (DependencyTreeNode child: this.parent.children) {
                if (child.equals(this) && child.dependecyParentToChild.equals("nn")) {
                    nouns.add(child);
                }
            }
        }

        levelOrderTraversal(nouns);

        for (int i = 0; i < nouns.size() - 1; i ++) {
            nouns.get(i).getWord().setNnNext(nouns.get(i + 1).getWord());
            nouns.get(i + 1).getWord().setNnPrev(nouns.get(i).getWord());
        }

        return this.word;
    }

    private List<DependencyTreeNode>
        levelOrderTraversal(List<DependencyTreeNode> nouns) {
        Queue<DependencyTreeNode> queue = new LinkedList<>();
        queue.offer(this);

        while (!queue.isEmpty()) {
            DependencyTreeNode currNode = queue.poll();
            for (DependencyTreeNode child: currNode.children) {
                if (this.getLinkNounCondition()) {
                    nouns.add(child);
                    queue.offer(child);
                }
            }
        }

        nouns.sort(Comparator.comparingInt(node -> node.getWord().getPosition()));
        return nouns;
    }

}
