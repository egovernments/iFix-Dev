# iFIX-Department-Entity-Service

## Department Hierarchy
It defines hierarchy definition for department.

**department id:** It is master department id like DWSS.\
**level:** It defines the depth of hierarchy of department level.\
**parent:** It provides details about department parent.

Root level department hierarchy will not contain any parent value and level value will be zero.\
Level value incrementation rule:
1. When parent id is having any value then we search parent in department hierarchy record for hierarchy level evaluation.
2. Get level value from parent department hierarchy and increment current department hierarchy level value by one.


## Department Entity
It contains department entity information along with its hierarchy level and also attaches master department information (department id - like DWSS).
Here we keep all children information list at every department node (department record).
Leaf level department will not have any children info.

Children list contains department entity id list, which makes current department entity parent of all children list (department id list), that's how it maintains department entity level.
