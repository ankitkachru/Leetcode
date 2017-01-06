package com.stevesun.common.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishercoder1534 on 9/30/16.
 */
public class UndirectedGraphNode {
    public int label;
    public List<UndirectedGraphNode> neighbors;
    public UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
}
