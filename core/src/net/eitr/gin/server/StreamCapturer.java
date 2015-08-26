public class StreamCapturer extends OutputStream {

    private StringBuilder buffer;
    private String prefix;
    private JTextArea consumer;
    private PrintStream old;

    public StreamCapturer(String prefix, JTextArea consumer, PrintStream old) {
        this.prefix = prefix;
        buffer = new StringBuilder(128);
        //buffer.append("[").append(prefix).append("] ");
        this.old = old;
        this.consumer = consumer;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);
        if (value.equals("\n")) {
            consumer.append(buffer.toString());
            buffer.delete(0, buffer.length());
            //buffer.append("[").append(prefix).append("] ");
        }
        old.print(c);
    }
    @Override
	public void write(int b) throws IOException {
	    buffer.append(Character.toChars((b + 256) % 256));
	    if ((char) b == '\n') {
	        consumer.append(str);
	        consumer.setCaretPosition(textArea.getDocument().getLength());
	        buffer.delete(0, buffer.length());
	    }
	    old.write(b);
	}
}    
