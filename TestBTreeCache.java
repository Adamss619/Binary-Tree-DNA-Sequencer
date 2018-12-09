
import java.util.NoSuchElementException;

public class TestBTreeCache {

    private static BTreeCache<String> cache;

    private static int totalTestRun;
    private static int testPassed;
    private static int testFailed;

    private static final String validElement_1 = "A";
    private static final String validElement_2 = "B";
    private static final String validElement_3 = "C";

    /**
     * Default constructor
     */
    public TestBTreeCache() {

        totalTestRun = 0;
        testPassed = 0;
        testFailed = 0;

        runTests();

    }

    /**
     * constructor with cache size
     *
     * @param cacheSize Cache size
     */
    public TestBTreeCache(int cacheSize) {

        testEmptyCache();
        testCacheWithOneElement();
        testCacheWithTwoElements();

    }

    /**
     * Run tests
     */
    private void runTests() {

        testEmptyCache();
        testCacheWithOneElement();
        testCacheWithTwoElements();

    }

    /**
     * Run cache tests with two elements
     */
    private void testCacheWithTwoElements() {

        cache = new BTreeCache<String>();
        cache.add(validElement_1);
        cache.add(validElement_2);

        System.out.println("\n********************************************************\n");
        System.out.println("Test Cache with two element...\n");

        testCacheWithTwoElements_addC_ABC();
        testCacheWithTwoElements_getB(validElement_2);
        testCacheWithTwoElements_removeLast();
        testCacheWithTwoElements_removeB();
        testCacheWithTwoElements_clear();

    }


    private void testCacheWithTwoElements_addC_ABC() {

        totalTestRun++;

        cache.add(validElement_3);

        if (cache.getSize() == 3) {
            testPassed++;
            System.out.println("Test: testCacheWithTwoElements_addC_ABC...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithTwoElements_addC_ABC...\t failed");
        }

    }

    private void testCacheWithTwoElements_getB(String Expected) {

        String result = cache.get(validElement_2);

        totalTestRun++;

        if (result == Expected) {
            testPassed++;
            System.out.println("Test: testCacheWithTwoElements_getB...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithTwoElements_getB...\t failed");
        }

    }

    private void testCacheWithTwoElements_removeLast() {

        totalTestRun++;

        cache.removeLast();

        if (cache.getSize() == 2) {
            testPassed++;
            System.out.println("Test: testCacheWithTwoElements_removeLast...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithTwoElements_removeLast...\t failed");
        }

    }

    private void testCacheWithTwoElements_removeB() {

        totalTestRun++;

        cache.remove(validElement_2);

        if (cache.getSize() == 1) {
            testPassed++;
            System.out.println("Test: testCacheWithTwoElements_removeB...\t passed");
            return;
        }

        testFailed++;
        System.out.println("Test: testCacheWithTwoElements_removeB...\t failed");

    }

    private void testCacheWithTwoElements_clear() {
        totalTestRun++;
        cache.clear();

        if (cache.isEmpty()) {
            testPassed++;
            System.out.println("Test: testCacheWithTwoElements_clear...\t passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithTwoElements_clear...\t failed");
        }

    }

    private void testCacheWithOneElement() {
        cache = new BTreeCache<String>();
        cache.add(validElement_1);

        System.out.println("\n********************************************************\n");
        System.out.println("Test Cache with one element...\n");

        testCacheWithOneElement_addB_BA();
        testCacheWithOneElement_getA(validElement_1);
        testCacheWithOneElement_removeLast();
        testCacheWithOneElement_removeA();
        testCacheWithOneElement_clear();

    }

    private void testCacheWithOneElement_addB_BA() {

        totalTestRun++;

        cache.add(validElement_2);

        if (cache.getSize() == 2) {
            testPassed++;
            System.out.println("Test: testCacheWithOneElement_addB_BA...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithOneElement_addB_BA...\t failed");
        }

    }

    private void testCacheWithOneElement_getA(String Expected) {
        String result = cache.get(validElement_1);

        totalTestRun++;

        if (result == Expected) {
            testPassed++;
            System.out.println("Test: testCacheWithOneElement_getA...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithOneElement_getA...\t failed");
        }

    }

    private void testCacheWithOneElement_removeLast() {
        totalTestRun++;

        cache.removeLast();

        if (cache.getSize() == 1) {
            testPassed++;
            System.out.println("Test: testCacheWithOneElement_removeLast...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithOneElement_removeLast...\t failed");
        }

    }

    private void testCacheWithOneElement_removeA() {

        totalTestRun++;

        cache.remove(validElement_1);

        if (cache.getSize() == 0) {
            testPassed++;
            System.out.println("Test: testCacheWithOneElement_removeA...\t passed");
            return;
        }

        testFailed++;
        System.out.println("Test: testCacheWithOneElement_removeA...\t failed");

    }

    private void testCacheWithOneElement_clear() {
        totalTestRun++;
        cache.clear();

        if (cache.isEmpty()) {
            testPassed++;
            System.out.println("Test: testCacheWithOneElement_clear...\t passed");
        } else {
            testFailed++;
            System.out.println("Test: testCacheWithOneElement_clear...\t failed");
        }

    }

    private void testEmptyCache() {

        cache = new BTreeCache<String>();

        System.out.println("\n********************************************************\n");
        System.out.println("Test Empty Cache...\n");

        emptyCache_addA_A();
        emptyCache_getA(validElement_1);
        emptyCache_removeLast();
        emptyCache_removeA();
        emptyCache_clear();

    }

    private void emptyCache_getA(String Expected) {
        String result = cache.get(validElement_1);

        totalTestRun++;

        if (result == Expected) {
            testPassed++;
            System.out.println("Test: emptyCache_getA...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: emptyCache_getA...\t failed");
        }
    }

    private void emptyCache_addA_A() {

        totalTestRun++;

        cache.add(validElement_1);

        if (cache.getSize() == 1) {
            testPassed++;
            System.out.println("Test: emptyCache_addA_A...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: emptyCache_addA_A...\t failed");
        }
    }

    private void emptyCache_removeLast() {

        totalTestRun++;

        cache.removeLast();

        if (cache.getSize() == 0) {
            testPassed++;
            System.out.println("Test: emptyCache_removeLast...\t Passed");
        } else {
            testFailed++;
            System.out.println("Test: emptyCache_removeLast...\t failed");
        }
    }

    private void emptyCache_removeA() {

        totalTestRun++;

        try {
            cache.remove(validElement_1);

        } catch (NoSuchElementException e) {

            testPassed++;
            System.out.println("Test: emptyCache_removeA...\t passed");
            return;
        }

        testFailed++;
        System.out.println("Test: emptyCache_removeA...\t failed");
    }

    private void emptyCache_clear() {

        totalTestRun++;
        cache.clear();

        if (cache.isEmpty()) {
            testPassed++;
            System.out.println("Test: emptyCache_clear...\t passed");
        } else {
            testFailed++;
            System.out.println("Test: emptyCache_clear...\t failed");
        }

    }

    public static void printSummary() {

        System.out.println("\n*******************************************");
        System.out.println("* Total Tests Run: " + totalTestRun);
        System.out.println("* Test Passed: " + testPassed);
        System.out.println("* Tests Failed: " + testFailed);
        System.out.println("*******************************************");

    }

    public static void main(String[] args) {

        System.out.println("\nTest BTreeCache with default Cache size...\n");

        new TestBTreeCache();

        System.out.println("\nTest BTreeCache with Cache size of 5...\n");

        new TestBTreeCache(5);

        printSummary();

    }

}
