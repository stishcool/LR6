public class Main {
    public static void main(String[] args)
    {
        ThreadProcessor processor = new ThreadProcessor();
        processor.computeMethodOne();
        processor.computeMethodTwo();
        processor.computeMethodThree(4);
    }
}