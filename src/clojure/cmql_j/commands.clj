(ns cmql-j.commands
  (:require cmql-core.read-write
            cmql-core.diagnostic
            cmql-core.administration
            cmql-core.roles
            cmql-core.operators.operators
            cmql-core.operators.stages
            cmql-j.internal.convert.commands-run
            [cmql-j.internal.convert.commands :refer [get-db-namespace get-command-info]]))

;;cMQL wrapping commands,main usage is portabillity,same code can run in js/java driver
;;driver methods are prefered unless there is a need for portability

;;-----------------------------------------Commands---------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

(defmacro dq [& args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (apply cmql-core.read-write/dq-f ~(vec args))))

(defmacro delete [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.read-write/delete ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro uq [& args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (apply cmql-core.read-write/uq-f ~(vec args))))

(defmacro update- [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.read-write/update- ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro find-and-modify [command-coll-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (get-command-info ~command-coll-info)
       (apply cmql-core.read-write/find-and-modify-f ~(vec (cons `(get-db-namespace ~command-coll-info) args))))))

(defmacro q-count [command-coll-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (get-command-info ~command-coll-info)
       (apply cmql-core.read-write/q-count-f ~(vec (cons `(get-db-namespace ~command-coll-info) args))))))

(defmacro q-distinct [command-coll-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (get-command-info ~command-coll-info)
       (apply cmql-core.read-write/q-distinct-f ~(vec (cons `(get-db-namespace ~command-coll-info) args))))))


;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------Admin-------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

;;commands

(defmacro create-view [command-coll-info new-view-name & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (get-command-info ~command-coll-info)
       (apply cmql-core.administration/create-view ~(vec (cons `(get-db-namespace ~command-coll-info) (cons new-view-name args)))))))

;;dsl no need?  TODO
(defmacro create-collection [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/create-collection ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

;;no need, driver is fine or driver wrapper, its useless
#_(defmacro rename-collection [command-coll-info target-coll-namespace & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/rename-collection ~(vec (cons `(get-db-namespace ~command-coll-info) (cons target-coll-namespace args))))))

(defmacro drop-collection [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/drop-collection ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro index [keys-vec & args]
  `(apply cmql-core.administration/index ~(vec (cons keys-vec args))))

(defmacro create-indexes [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/create-indexes ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro create-index [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/create-index ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro drop-indexes [command-coll-info indexnames-key-vecs & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/drop-indexes ~(vec (cons `(get-db-namespace ~command-coll-info) (cons indexnames-key-vecs args))))))

(defmacro drop-index [command-coll-info indexnames-key-vecs & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/drop-index ~(vec (cons `(get-db-namespace ~command-coll-info) (cons indexnames-key-vecs args))))))

(defmacro kill-cursors [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.administration/kill-cursors ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro list-collections [command-db-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (keyword (str (name ~command-db-info) "."))
       (apply cmql-core.administration/list-collections ~(vec (cons command-db-info args))))))

(defmacro drop-database [command-db-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.administration/drop-database ~(vec (cons command-db-info args)))))

(defmacro list-databases [ & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-command
       (get-command-info :admin)
       (apply cmql-core.administration/list-databases ~(vec args)))))

(defmacro shutdown-database [& args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info :admin)
     (apply cmql-core.administration/shutdown-database ~(vec args))))

(defmacro current-op [& args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info :admin)
     (apply cmql-core.administration/current-op ~(vec args))))

(defmacro kill-op [opid-number & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info :admin)
     (apply cmql-core.administration/kill-op ~(vec (cons opid-number args)))))


;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------Diagnostic----------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

;;commands
(defmacro coll-stats [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.diagnostic/coll-stats ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro coll-data-size [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.diagnostic/coll-data-size ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro caped? [command-coll-info]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.diagnostic/caped? ~(vec `(get-db-namespace ~command-coll-info)))))

(defmacro storage-size [command-coll-info]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.diagnostic/storage-size ~(vec `(get-db-namespace ~command-coll-info)))))

(defmacro validate [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info ~command-coll-info)
     (apply cmql-core.diagnostic/validate ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))

(defmacro server-status [& args]
  `(cmql-j.internal.convert.commands-run/run-command
     (get-command-info :admin)
     (apply cmql-core.diagnostic/server-status ~(vec args))))


;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------Users-------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

;;commands
(defmacro create-user [command-db-info command-map & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/create-user ~(vec (cons command-db-info (cons command-map args))))))

(defmacro drop-all-users-from-database [command-db-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/drop-all-users-from-database ~(vec (cons command-db-info args)))))

(defmacro drop-user [command-db-info username & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/drop-user ~(vec (cons command-db-info (cons username args))))))

(defmacro grant-roles [command-db-info username roles & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/drop-user ~(vec (cons command-db-info (cons username (cons roles args)))))))

(defmacro revoke-roles [command-db-info username roles & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/revoke-roles ~(vec (cons command-db-info (cons username (cons roles args)))))))

(defmacro users-info [command-db-info command-map & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.users/users-info ~(vec (cons command-db-info (cons command-map args))))))


;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------Roles-------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

;;commands
(defmacro create-role [command-db-info command-map & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/create-role ~(vec (cons command-db-info (cons command-map args))))))

(defmacro update-role [command-db-info command-map & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/update-role ~(vec (cons command-db-info (cons command-map args))))))

(defmacro drop-role [command-db-info rolename & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/update-role ~(vec (cons command-db-info (cons rolename args))))))

(defmacro drop-all-roles-from-database [command-db-info & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/drop-all-roles-from-database ~(vec (cons command-db-info args)))))

(defmacro grant-privileges-to-role [command-db-info rolename privileges & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/grant-privileges-to-role
            ~(vec (cons command-db-info (cons rolename (cons privileges args)))))))

(defmacro grant-roles-to-role [command-db-info rolename roles & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/grant-roles-to-role
            ~(vec (cons command-db-info (cons rolename (cons roles args)))))))

(defmacro revoke-privileges-from-role [command-db-info rolename privileges & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/revoke-privileges-from-role
            ~(vec (cons command-db-info (cons rolename (cons privileges args)))))))

(defmacro revoke-role-from-role [command-db-info rolename privileges & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/revoke-role-from-role
            ~(vec (cons command-db-info (cons rolename (cons privileges args)))))))

(defmacro roles-info [command-db-info command-map & args]
  `(cmql-j.internal.convert.commands-run/run-command
     (keyword (str (name ~command-db-info) "."))
     (apply cmql-core.roles/roles-info ~(vec (cons command-db-info (cons command-map args))))))


;;---------------------------------Methods Called as Commands(insert depends on arguments)------------------------------
;;----------------------------------------------------------------------------------------------------------------------
;;----------------------------------------------------------------------------------------------------------------------

;;Methods

;;TODO DSL can be moved bellow to not include the get-command-info call

(defmacro fq [command-coll-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-find
       (get-command-info ~command-coll-info)
       (apply cmql-core.read-write/fq-f ~(vec (cons `(get-db-namespace ~command-coll-info) args))))))

(defmacro q [command-coll-info & args]
  `(let ~cmql-core.operators.operators/operators-mappings
     (cmql-j.internal.convert.commands-run/run-aggregation
       (get-command-info ~command-coll-info)
       (apply cmql-core.read-write/q-f ~(vec (cons `(get-db-namespace ~command-coll-info) args))))))

(defmacro insert [command-coll-info & args]
  `(cmql-j.internal.convert.commands-run/run-insert-method-or-command
     ~command-coll-info
     (apply cmql-core.read-write/insert ~(vec (cons `(get-db-namespace ~command-coll-info) args)))))
