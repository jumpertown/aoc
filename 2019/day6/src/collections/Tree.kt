package collections

class TreeNode<T>(val value:T) {
    var parent: TreeNode<T>? = null

    var children: MutableList<TreeNode<T>> = mutableListOf()

    fun addChild(node:TreeNode<T>) {
        this.children.add(node)
        node.parent = this
    }

    fun depth(): Int {
        tailrec fun getDepth(node: TreeNode<T>, currentDepth: Int): Int =
            if(node.parent == null)
                currentDepth
            else
                getDepth(node.parent!!, currentDepth + 1)

        return getDepth(this, 0)
    }

    fun ancestors(): List<TreeNode<T>> =
        if(this.parent == null)
            listOf<TreeNode<T>>()
        else
            listOf(this.parent!!) + this.parent!!.ancestors()

    fun commonAncestor(other: TreeNode<T>): TreeNode<T>? {
        val otherAncestors = other.ancestors().toSet().map {it.value}

        if(otherAncestors.contains(this.value))
            return this

        if(this.ancestors().map {it.value}.contains(other.value))
            return other

        for(ancestor in this.ancestors()) {
            if (otherAncestors.contains(ancestor.value))
                return ancestor
        }

        //No common ancestor
        return null
    }


    override fun toString(): String = "<TreeNode $value>"
}