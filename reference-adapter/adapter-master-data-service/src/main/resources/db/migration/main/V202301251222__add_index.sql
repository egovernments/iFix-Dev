CREATE INDEX IF NOT EXISTS index_department_id ON department (id);
CREATE INDEX IF NOT EXISTS index_department_code ON department (code);
CREATE INDEX IF NOT EXISTS index_department_name ON department (name);
CREATE INDEX IF NOT EXISTS index_department_is_nodal ON department (is_nodal);
CREATE INDEX IF NOT EXISTS index_department_parent ON department (parent);
CREATE INDEX IF NOT EXISTS index_department_tenant_id ON department (tenant_id);


CREATE INDEX IF NOT EXISTS index_expenditure_id ON expenditure (id);
CREATE INDEX IF NOT EXISTS index_expenditure_code ON expenditure (code);
CREATE INDEX IF NOT EXISTS index_expenditure_department_id ON expenditure (department_id);
CREATE INDEX IF NOT EXISTS index_expenditure_name ON expenditure (name);
CREATE INDEX IF NOT EXISTS index_expenditure_tenant_id ON expenditure (tenant_id);
CREATE INDEX IF NOT EXISTS index_expenditure_type ON expenditure (type);


CREATE INDEX IF NOT EXISTS index_project_id ON project (id);
CREATE INDEX IF NOT EXISTS index_project_code ON project (code);
CREATE INDEX IF NOT EXISTS index_project_expenditure_id ON project (expenditure_id);
CREATE INDEX IF NOT EXISTS index_project_name ON project (name);
CREATE INDEX IF NOT EXISTS index_project_tenant_id ON project (tenant_id);


CREATE INDEX IF NOT EXISTS index_project_department_entity_relationship_project_id ON project_department_entity_relationship (project_id);
CREATE INDEX IF NOT EXISTS index_project_department_entity_relationship_department_entity_id ON project_department_entity_relationship (department_entity_id);
CREATE INDEX IF NOT EXISTS index_project_department_entity_relationship_status ON project_department_entity_relationship (status);

