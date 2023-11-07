import java.util.Arrays;

public class ThreadProcessor {
    static final int arraySize = 60_000_000;
    static final int halfSize = arraySize / 2;

    public void computeMethodOne()
    {
        float[] dataArray = new float[arraySize];
        Arrays.fill(dataArray, 1.0f);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dataArray.length; i++)
        {
            dataArray[i] = (float) (dataArray[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Method One:");
        System.out.println("First element: " + dataArray[0]);
        System.out.println("Last element: " + dataArray[dataArray.length - 1]);
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public void computeMethodTwo()
    {
        float[] dataArray = new float[arraySize];
        float[] firstHalfArray = new float[halfSize];
        float[] secondHalfArray = new float[halfSize];
        Arrays.fill(dataArray, 1.0f);
        long startTime = System.currentTimeMillis();
        System.arraycopy(dataArray, 0, firstHalfArray, 0, halfSize);
        System.arraycopy(dataArray, halfSize, secondHalfArray, 0, halfSize);

        Thread threadOne = new Thread(() -> {
            for (int i = 0; i < firstHalfArray.length; i++) {
                firstHalfArray[i] = (float) (firstHalfArray[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
            System.arraycopy(firstHalfArray, 0, dataArray, 0, firstHalfArray.length);
        });
        Thread threadTwo = new Thread(() -> {
            for (int i = 0; i < secondHalfArray.length; i++) {
                secondHalfArray[i] = (float) (secondHalfArray[i] * Math.sin(0.2f + (halfSize + i) / 5) * Math.cos(0.2f + (halfSize + i) / 5) * Math.cos(0.4f + (halfSize + i) / 2));
            }
            System.arraycopy(secondHalfArray, 0, dataArray, halfSize, secondHalfArray.length);
        });
        threadOne.start();
        threadTwo.start();
        try {
            threadOne.join();
            threadTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Method Two:");
        System.out.println("First element: " + dataArray[0]);
        System.out.println("Last element: " + dataArray[dataArray.length - 1]);
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public void computeMethodThree(int n)
    {
        float[] dataArray = new float[arraySize];
        Arrays.fill(dataArray, 1.0f);
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[n];
        float[][] subResults = new float[n][];
        int partSize = arraySize / n;

        for (int i = 0; i < n; i++) {
            final int startIndex = i * partSize;
            final int endIndex = (i == n - 1) ? arraySize : startIndex + partSize;
            final int index = i;

            threads[i] = new Thread(() -> {
                float[] subArray = Arrays.copyOfRange(dataArray, startIndex, endIndex);
                for (int j = 0; j < subArray.length; j++) {
                    subArray[j] = (float) (subArray[j] * Math.sin(0.2f + (startIndex + j) / 5) * Math.cos(0.2f + (startIndex + j) / 5) * Math.cos(0.4f + (startIndex + j) / 2));
                }
                subResults[index] = subArray;
            });
            threads[i].start();
        }
        try {
            for (Thread thread : threads)
            {
                thread.join();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < n; i++) {
            System.arraycopy(subResults[i], 0, dataArray, i * partSize, subResults[i].length);
        }
        System.out.println("Method Three:");
        System.out.println("First element: " + dataArray[0]);
        System.out.println("Last element: " + dataArray[dataArray.length - 1]);
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
