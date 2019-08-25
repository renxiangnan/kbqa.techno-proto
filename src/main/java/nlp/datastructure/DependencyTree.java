package nlp.datastructure;

import nlp.utils.MaltParser;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.DependencyStructure;
import org.maltparser.core.syntaxgraph.node.DependencyNode;

import java.util.*;

public class DependencyTree {
    private DependencyTreeNode root;
    private List<DependencyTreeNode> allNodes;
    private DependencyStructure maltDependencyStructure;
    private Map<Integer, DependencyTreeNode> treeNodeIdMap;
    private Sentence sentence;

    public List<DependencyTreeNode> getAllNodes() { return this.allNodes; }

    private List<List<DependencyTreeNode>>
        levelOrderTranversal(DependencyStructure dependencyGraph) throws MaltChainedException {
        List<List<DependencyTreeNode>> nodeList = new ArrayList<>();
        DependencyNode root = dependencyGraph.getDependencyRoot();
        DependencyNode currNode;
        Queue<DependencyNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<DependencyTreeNode> levelNodeList = new ArrayList<>();

            for (int i = 0; i < size; i ++) {
                currNode = queue.poll();
                assert currNode != null;
                int currId = currNode.getIndex();

                if (currNode.hasHead() && !treeNodeIdMap.containsKey(currId)) {
                    String edgeStr = currNode.getHeadEdge().toString();
                    int ind1 = edgeStr.lastIndexOf(':') + 1;
                    int ind2 = edgeStr.lastIndexOf(' ');
                    String dependency = edgeStr.substring(ind1, ind2);

                    if (dependency.equals(DependencyTreeLabel.EMPTY_LABEL)){
                        dependency = "";
                    } else if (dependency.equals(DependencyTreeLabel.PUNCT_LABEL)) {
                        continue;
                    }

                    DependencyTreeNode newNode = new DependencyTreeNode(sentence.getWordByIndex(currId));
                    newNode.setDependecyParentToChild(dependency);
                    treeNodeIdMap.put(currId, newNode);
                    levelNodeList.add(newNode);
                }

                while (currNode != null) {
                    if (currNode.getClosestLeftDependent() != null) { queue.offer(currNode.getClosestLeftDependent()); }
                    if (currNode.getClosestRightDependent() != null) { queue.offer(currNode.getClosestRightDependent()); }
                    currNode = currNode.getRightSibling();
                }
            }

            for (DependencyTreeNode dependencyTreeNode : levelNodeList) {
                dependencyTreeNode.setHeight(nodeList.size() - 1);
            }

            nodeList.add(levelNodeList);
        }
        return nodeList;
    }

    /*
        1. Add edges
        2. Add level
     */
    private void complementTree() {

        for (Map.Entry<Integer, DependencyTreeNode> entry: treeNodeIdMap.entrySet()) {
            int nodeId = entry.getKey();
            DependencyTreeNode treeNode = entry.getValue();
            DependencyNode dependencyNode;

            try {
                dependencyNode = maltDependencyStructure.getDependencyNode(nodeId);

                if (treeNode.getDependecyParentToChild().length() == 0){
                    this.setRoot(treeNode);
                    this.root.setHeight(0);
                    this.root.setDependecyParentToChild(DependencyTreeLabel.ROOT_LABEL);
                } else {
                    DependencyTreeNode parentTreeNode = treeNodeIdMap.get(dependencyNode.getHead().getIndex());
                    DependencyTreeNode childTreeNode = treeNodeIdMap.get(dependencyNode.getIndex());
                    childTreeNode.setParent(parentTreeNode);
                    parentTreeNode.getChildren().add(childTreeNode);
                }
            } catch (MaltChainedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<DependencyTreeNode> flatAllNodes(List<List<DependencyTreeNode>> nodeList) {
        List<DependencyTreeNode> allNodes = new ArrayList<>();
        for (List<DependencyTreeNode> nodesBylevel: nodeList) {
            for (DependencyTreeNode node: nodesBylevel) {
                node.linkNoun();
            }
            allNodes.addAll(nodesBylevel);
        }

        allNodes.sort(Comparator.comparingInt(node -> node.getWord().getPosition()));
        return allNodes;
    }

    public DependencyTree(Sentence sentence, MaltParser maltParser) throws MaltChainedException {
        this.sentence = sentence;
        this.maltDependencyStructure = maltParser.getDependencyStructure(sentence);
        this.treeNodeIdMap = new HashMap<>();
        List<List<DependencyTreeNode>> nodeList = levelOrderTranversal(this.maltDependencyStructure);
        complementTree();
        this.allNodes = flatAllNodes(nodeList);
    }


    public void setRoot(DependencyTreeNode root) { this.root = root; }

    public List<DependencyTreeNode> getPathToRoot(DependencyTreeNode node) {
        List<DependencyTreeNode> path = new ArrayList<>();
        DependencyTreeNode currNode = node;
        path.add(currNode);

        while (currNode.getParent() != null) {
            currNode = currNode.getParent();
            path.add(currNode);
        }
        return path;
    }

    public List<DependencyTreeNode> getUndirectedShortestPath(DependencyTreeNode node1,
                                                              DependencyTreeNode node2) {
        if (node1.equals(node2)) return new ArrayList<>();
        List<DependencyTreeNode> path1 = getPathToRoot(node1);
        List<DependencyTreeNode> path2 = getPathToRoot(node2);
        List<DependencyTreeNode> shortestPath = new ArrayList<>();

        int ind1 = path1.size() - 1, ind2 = path2.size() - 1;
        DependencyTreeNode currNode1 = path1.get(ind1);
        DependencyTreeNode currNode2 = path2.get(ind2);

        while (currNode1.equals(currNode2)) {
            ind1 --;
            ind2 --;

            if(ind1 < 0 || ind2 < 0) break;
            currNode1 = path1.get(ind1);
            currNode2 = path2.get(ind2);
        }

        for (int i = 0; i <= ind1; i ++) { shortestPath.add(path1.get(i)); }
        for (int i = ind2 + 1; i >= 0; i --) { shortestPath.add(path2.get(i)); }

        return shortestPath;
    }

    public DependencyTreeNode getNodeByIndex(int position) {
        for (DependencyTreeNode node: allNodes) {
            if (node.getWord().getPosition() == position) {
                return node;
            }
        }
        return null;
    }

}


interface DependencyTreeLabel {

    String EMPTY_LABEL = "null";
    String ROOT_LABEL = "root";
    String PUNCT_LABEL = "punct";

}