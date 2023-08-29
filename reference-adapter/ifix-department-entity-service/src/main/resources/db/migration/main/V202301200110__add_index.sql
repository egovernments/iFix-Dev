CREATE INDEX IF NOT EXISTS index_department_entity_id ON department_entity (id);
CREATE INDEX IF NOT EXISTS index_department_entity_tenant_id ON department_entity (tenant_id);
CREATE INDEX IF NOT EXISTS index_department_entity_department_id ON department_entity (department_id);
CREATE INDEX IF NOT EXISTS index_department_entity_hierarchy_level ON department_entity (hierarchy_level);
CREATE INDEX IF NOT EXISTS index_department_entity_code ON department_entity (code);
CREATE INDEX IF NOT EXISTS index_department_entity_name ON department_entity (name);

CREATE INDEX IF NOT EXISTS index_department_entity_children_parent_id ON department_entity_children (parent_id);
CREATE INDEX IF NOT EXISTS index_department_entity_children_child_id ON department_entity_children (child_id);

CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_id ON department_hierarchy_level (id);
CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_department_id ON department_hierarchy_level (department_id);
CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_label ON department_hierarchy_level (label);
CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_level ON department_hierarchy_level (level);
CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_parent ON department_hierarchy_level (parent);
CREATE INDEX IF NOT EXISTS index_department_hierarchy_level_tenant_id ON department_hierarchy_level (tenant_id);
