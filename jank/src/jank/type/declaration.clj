(ns jank.type.declaration)

(defn shorten-types [item]
  "Walks through the decl and replaces all [:type ...] instances with
   their shorter type names. Example: [:type [:identifier \"string\"]]
   becomes (\"string\")"
  (clojure.walk/postwalk
    (fn [x]
      (if (and (vector? x) (= :type (first x)))
        (rest (second x))
        x))
    item))

(defn lookup-binding [decl-name scope]
  "Recursively looks through the hierarchy of scopes for the declaration."
  (loop [current-scope scope]
    (when current-scope
      ; TODO: Should be a vector of overloads
      (if-let [found (find (:binding-declarations current-scope) decl-name)]
        found
        (recur (:parent current-scope))))))

(defn validate-binding [decl-name decl-type scope]
  "Looks up a declaration, if any, and verifies that the provided
   declaration has a matching type. Returns the decl or nil, if none is found."
  (let [decl (lookup-binding decl-name scope)]
    (when (some? decl)
      (let [expected-type (:type (second decl))]
        ; TODO: Allow overloads
        (assert (= expected-type decl-type)
                (str "Declaration of "
                     decl-name
                     " doesn't match previous declarations: "
                     expected-type
                     " vs "
                     decl-type))))
    decl))

(defn lookup-type [decl-name scope]
  "Recursively looks through the hierarchy of scopes for the declaration."
  (loop [current-scope scope]
    ; TODO: Handle functions and other generic types properly
    (when current-scope
      (if-let [found (contains? (:type-declarations current-scope) decl-name)]
        found
        (recur (:parent current-scope))))))

(defmulti add-to-scope
  (fn [item scope]
    (let [kind (first (second item))]
      (cond
        (= :type kind)
        :type-declaration
        (= :identifier kind)
        :binding-declaration
        :else
        (assert false (str "Invalid binding: " item))))))

(defmethod add-to-scope :type-declaration [item scope]
  "Adds the opaque type declaration to the scope.
   Returns the updated scope."
  (let [decl-name (first (shorten-types (rest item)))]
    (update scope :type-declarations conj decl-name)))

(defmethod add-to-scope :binding-declaration [item scope]
  "Finds, validates, and adds the provided declaration into the scope.
   Returns the updated scope."
  (let [shortened (shorten-types item)
        decl-name (get-in shortened [1 1])
        decl-type (get-in shortened [2])
        found-decl (validate-binding decl-name decl-type scope)
        found-type (lookup-type decl-type scope)]
    (assert (some? found-type) (str "Unknown type: " decl-type))
    (if (nil? found-decl)
      (update scope :binding-declarations assoc decl-name {:type decl-type})
      scope)))