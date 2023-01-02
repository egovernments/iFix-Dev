# iFIX-Department-Entity-Service

## [API-Contract Link](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/egovernments/iFix-Dev/develop/domain-services/ifix-department-entity-service/ifix-department-entity-service-1.0.0.yaml)
## [Postman Collection Link](https://www.getpostman.com/collections/b330dc3698bf009d2ef5)

## Department Hierarchy
It defines hierarchy definition for department.

**department id:** It is master department id (UUID).\
**level:** It defines the depth of hierarchy of department level.\
**parent:** It provides details about department parent (UUID).

Root level department hierarchy will not contain any parent value and level value will be zero.\
Level value incrementation rule:
1. When parent id is having any value then we search parent in department hierarchy record for hierarchy level evaluation.
2. Get level value from parent department hierarchy and increment current department hierarchy level value by one.


## Department Entity
It contains department entity information along with its hierarchy level and also attaches master department information (department id - UUID).
Here we keep all children information list at every department node (department record).
Leaf level department will not have any children info.

Children list contains department entity id list, which makes current department entity parent of all children list (department id list), that's how it maintains department entity level.

### Entities creation approaches
1. First we need to define the hierarchy level top to bottom because it has parent's reference.
2. Only then we can start adding department entities, bottom-to-top because it has child's reference.

_**Note**: 
1. The root department hierarchy will have the label "Department", and the root department entity will be the
department itself._
2. When we have to update the existing children list of Department Entity then update the existing children list using mongodb command like below:

   **Steps**: 
             
             1. Find the department entity parent where the new children need to be added. Do search by name and hierarchy level.
              
              db.departmentEntityDTO.find({"name" : "<current_department_entity's_name>","hierarchyLevel": <Current_department_entity_hierarchy_level>});
              
              2. Append the department entity id at the end of the current department entity children's list. So first find the length of the current array and then set it as
                 "children.n": "<Resulted_department_entity_id>" (where n is the length of the array.). 
             
             e.g: 
             db.departmentEntityDTO.update({"_id": "<current_department_entity_id>"},{$set:
             {"children.n": "<Resulted_department_entity_id_1>",
             "children.n+1": "<Resulted_department_entity_id_2>"}
             })