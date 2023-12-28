class Node {
    String phoneNumber;
    String contactName;
    Node parent;
    Node left;
    Node right;
    int color; // 0 for black, 1 for red

    public Node(String phoneNumber, String contactName) {
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.color = 1; // New nodes are always red. if 0 == black
        this.parent = null;
        this.left = null;
        this.right = null;
    }
}

class RedBlackTree {
    private Node root;
    private Node TNULL;

    // Constructor
    public RedBlackTree() {
        TNULL = new Node("", "");
        TNULL.color = 0;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }


    // Getter untuk mendapatkan TNULL
    public Node getTNULL() {
        return this.TNULL;
    }

    // Metode penyisipan (insert) untuk menambahkan node baru ke dalam pohon
    public void insertTree(String phoneNumber, String contactName) {
        Node node = new Node(phoneNumber, contactName);
        node.parent = null;
        node.left = TNULL;
        node.right = TNULL;
        node.color = 1; // New nodes are always red

        Node y = null;
        Node x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.phoneNumber.compareTo(x.phoneNumber) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.phoneNumber.compareTo(y.phoneNumber) < 0) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = 0;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    // Get the root of the tree
    public Node getRoot() {
        return this.root;
    }

    // Fix the rb tree
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == 1) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // uncle
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right; // uncle

                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent; // Move to the grandparent
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

    // Left rotate
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Right rotate
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Print the tree
    private void printTreeHelper(Node root, String indent, boolean last) {
        if (root != TNULL) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }

            String sColor = (root.color == 1) ? "RED" : "BLACK";
            System.out.println(root.phoneNumber + " (" + sColor + ")");
            printTreeHelper(root.left, indent, false);
            printTreeHelper(root.right, indent, true);
        }
    }

    // Public method to display the tree
    public void printTree() {
        printTreeHelper(this.root, "", true);
    }

    // Search
    private Node searchTreeHelper(Node node, String contactName) {
        if (node == TNULL || contactName.equals(node.contactName)) {
            return node;
        }

        if (contactName.compareTo(node.contactName) < 0) {
            return searchTreeHelper(node.left, contactName);
        }
        return searchTreeHelper(node.right, contactName);
    }

    // Search
    public Node search(String contactName) {
        return searchTreeHelper(this.root, contactName);
    }

    // Other methods for Red-Black Tree...

    // Transplant a node
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    // Delete a node from the tree
    private void deleteNodeHelper(Node node, String phoneNumber) {
        Node z = TNULL;
        Node x, y;
        while (node != TNULL) {
            if (node.phoneNumber.equals(phoneNumber)) {
                z = node;
            }

            if (phoneNumber.compareTo(node.phoneNumber) <= 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find phone number in the tree");
            return;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            fixDelete(x);
        }
    }

    // Balance the tree after deletion
    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == 0 && s.right.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == 0 && s.right.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }

    // Minimum
    private Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }
}

class PhoneBook {
    private RedBlackTree phoneBookTree;

    // Konstruktor untuk membuat instance PhoneBook baru
    public PhoneBook() {
        phoneBookTree = new RedBlackTree();
    }

    // Add a new entry to the phone book
    public void addEntry(String phoneNumber, String contactName) {
        phoneBookTree.insertTree(phoneNumber, contactName);
    }

    // Search for a phone number by contact name
    public String searchPhoneNumber(String phoneNumber) {
        Node result = phoneBookTree.search(phoneNumber);
        if (result != null) {
            return result.phoneNumber;
        } else {
            return "Phone number not found for " + phoneNumber;
        }
    }

    // Display the entire phone book in order
    public void displayPhoneBook() {
        System.out.println("Phone Book:");
        displayPhoneBookRecursive(phoneBookTree.getRoot());
    }

    private void displayPhoneBookRecursive(Node node) {
        if (node != phoneBookTree.getTNULL()) {
            displayPhoneBookRecursive(node.left);

            System.out.println("Contact Name: " + node.contactName +
                    ", Phone Number: " + node.phoneNumber +
                    ", Color: " + (node.color == 1 ? "RED" : "BLACK"));

            displayPhoneBookRecursive(node.right);
        }
    }

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();

        // Add entries to the phone book
        phoneBook.addEntry("1234567890", "John Doe");
        phoneBook.addEntry("9876543210", "Jane Doe");
        phoneBook.addEntry("5551234567", "Fadya");
        phoneBook.addEntry("1112223334", "Budi Santoso");
        phoneBook.addEntry("5558889999", "Citra Dewi");
        phoneBook.addEntry("7773331111", "Dwi Cahyono");
        phoneBook.addEntry("4446668888", "Eka Putri");
        phoneBook.addEntry("9991112222", "Faisal Ramadhan");
        phoneBook.addEntry("8884446666", "Gita Perwira");
        phoneBook.addEntry("2225557777", "Hendra Gunawan");

        // Tampilkan buku telepon setelah penambahan
        phoneBook.displayPhoneBook();

        // Lakukan pencarian nomor telepon berdasarkan nama
        System.out.println("\nSearching for phone number for Fadya: " + phoneBook.searchPhoneNumber("Fadya"));
        System.out.println("Searching for phone number for Budi Santoso: " + phoneBook.searchPhoneNumber("Budi Santoso"));

        // ... (tambahkan operasi lain sesuai kebutuhan)
    }
}
