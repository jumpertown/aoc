import collections.TreeNode


val a = TreeNode("A")
val b = TreeNode("B")
val c = TreeNode("C")
val d = TreeNode("D")

a

a.addChild(b)
a.addChild(c)
b.addChild(d)

a.depth()
b.depth()
d.depth()

a.ancestors()
a.children
b.parent
d.ancestors()

d.commonAncestor(c)

a.depth()









