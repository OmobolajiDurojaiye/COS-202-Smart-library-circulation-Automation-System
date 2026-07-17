package controller;

import model.LibraryItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortEngine {

    public static Comparator<LibraryItem> byTitle() { return Comparator.comparing(i -> i.getTitle().toLowerCase()); }
    public static Comparator<LibraryItem> byAuthor() { return Comparator.comparing(i -> i.getAuthor().toLowerCase()); }
    public static Comparator<LibraryItem> byYear() { return Comparator.comparingInt(LibraryItem::getYear); }

    // 1. Selection Sort
    public static void selectionSort(List<LibraryItem> list, Comparator<LibraryItem> cmp) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (cmp.compare(list.get(j), list.get(minIdx)) < 0) minIdx = j;
            }
            swap(list, i, minIdx);
        }
    }

    // 2. Insertion Sort
    public static void insertionSort(List<LibraryItem> list, Comparator<LibraryItem> cmp) {
        for (int i = 1; i < list.size(); i++) {
            LibraryItem key = list.get(i);
            int j = i - 1;
            while (j >= 0 && cmp.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // 3. Merge Sort (recursive)
    public static void mergeSort(List<LibraryItem> list, Comparator<LibraryItem> cmp) {
        if (list.size() < 2) return;
        List<LibraryItem> temp = new ArrayList<>(list);
        mergeSortHelper(list, temp, 0, list.size() - 1, cmp);
    }

    private static void mergeSortHelper(List<LibraryItem> list, List<LibraryItem> temp,
                                         int left, int right, Comparator<LibraryItem> cmp) {
        if (left >= right) return; // base case
        int mid = (left + right) / 2;
        mergeSortHelper(list, temp, left, mid, cmp);
        mergeSortHelper(list, temp, mid + 1, right, cmp);
        merge(list, temp, left, mid, right, cmp);
    }

    private static void merge(List<LibraryItem> list, List<LibraryItem> temp,
                               int left, int mid, int right, Comparator<LibraryItem> cmp) {
        for (int i = left; i <= right; i++) temp.set(i, list.get(i));
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (cmp.compare(temp.get(i), temp.get(j)) <= 0) list.set(k++, temp.get(i++));
            else list.set(k++, temp.get(j++));
        }
        while (i <= mid) list.set(k++, temp.get(i++));
        while (j <= right) list.set(k++, temp.get(j++));
    }

    // 4. Quick Sort (recursive)
    public static void quickSort(List<LibraryItem> list, Comparator<LibraryItem> cmp) {
        quickSortHelper(list, 0, list.size() - 1, cmp);
    }

    private static void quickSortHelper(List<LibraryItem> list, int low, int high, Comparator<LibraryItem> cmp) {
        if (low >= high) return; // base case
        int pivotIdx = partition(list, low, high, cmp);
        quickSortHelper(list, low, pivotIdx - 1, cmp);
        quickSortHelper(list, pivotIdx + 1, high, cmp);
    }

    private static int partition(List<LibraryItem> list, int low, int high, Comparator<LibraryItem> cmp) {
        LibraryItem pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (cmp.compare(list.get(j), pivot) <= 0) { i++; swap(list, i, j); }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private static void swap(List<LibraryItem> list, int a, int b) {
        LibraryItem tmp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, tmp);
    }
}
