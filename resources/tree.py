# Stolen shamelessly from Ben:
# https://github.com/willcodefortea/pytudes/blob/master/ipynb/Advent%20of%20Code%202017.ipynb


class Node(object):
    def __init__(self, name):
        self.name = name
        self._children = []
        self.parent = None
        
    def __repr__(self):
        return '<Node 0x:{} name={}>'.format(
            id(self),
            self.name
        )
    
    @property
    def children(self):
        """Immutable set of the node's children."""
        return tuple(self._children)
    
    def add_child(self, child):
        self._children.append(child)
    
    def remove_child(self, child):
        self._children.remove(child)
    
    def add_parent(self, parent):
        if self.parent:
            self.parent.remove_child(self)

        self.parent = parent

        if self.parent:
            self.parent.add_child(self)
    
    @property
    def root(self):
        if not self.parent:
            return self
        return self.parent.root
    
    @property
    def decendants(self):
        """Visit all children."""
        iterator = visit_pre_order(self)
        # Skip this node
        next(iterator)
        return iterator
    
    @property
    def siblings(self):
        for child in self.parent.children:
            if child == self:
                continue
            yield child

    
def visit_pre_order(node):
    """Visit a Tree in pre-order.
    
      A
     / \
    B   C
    
    A -> B -> C
    """
    yield node
    for child in node.children:
        yield from visit_pre_order(child)
