package br.com.eduardothomaz.algorithms;

public final class Demo {
    private Demo() {
    }

    public static void main(String[] args) {
        DivisionHashTable<Integer, String> hash = new DivisionHashTable<Integer, String>(5);
        hash.put(10, "dez");
        hash.put(15, "quinze");
        hash.put(20, "vinte");

        AvlTree<Integer, String> avl = new AvlTree<Integer, String>();
        int[] values = {30, 20, 10, 25, 40, 50};
        for (int value : values) {
            avl.put(value, "valor-" + value);
        }

        System.out.println("Hash: chave 15 na gaveta " + hash.bucketIndex(15));
        System.out.println("Hash: valor da chave 20 = " + hash.get(20));
        System.out.println("AVL em ordem = " + avl.keysInOrder());
        System.out.println("AVL altura = " + avl.height() + ", balanceada = " + avl.isBalanced());
    }
}
