CREATE TABLE Ticket (
id INTEGER NOT NULL,
customer_name VARCHAR(50),
subject VARCHAR(50) NOT NULL,
body VARCHAR(200) NOT NULL,


);



    private long id;
    private String customerName;
    private String subject;
    private String body;
    private Map<String, Attachment> attachments = new LinkedHashMap<>();
    private List replyId;

    private String name;
    private String mimeContentType;
    private byte[] contents;