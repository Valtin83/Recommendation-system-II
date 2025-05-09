databaseChangeLog:
  - changeSet:
      id: 1
      author: zhevalentin
      changes:
        - createTable:
            tableName: rules
            columns:
              - column:
                  name: id
                  type: uuid
                  constraints:
                    primaryKey: true
                    nullable: false
              - column:
                  name: product_name
                  type: varchar(255)
                  constraints:
                    nullable: false
              - column:
                  name: product_id
                  type: varchar(255)
                  constraints:
                    nullable: false
              - column:
                  name: product_text
                  type: text
                  constraints:
                    nullable: true
              - column:
                  name: rule
                  type: jsonb
                  constraints:
                    nullable: false