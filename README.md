# Email-Backend

#Team Members:\
    Ali Hassan ElSharawy\
    Basel Ahmed\
    Louay Magdy
  
Design Patterns used:
    1- Delegation Pattern:\
       - Relation between services and controllers\
       - Relation between services and models\
       - Relation between some services such as userFileService and MessageService
  
    2- Interface Pattern:
       - Relation between MessageCriteria class and MessageService\
       - Relation between UserCriteria class and UserService
     
    3- Prototype Pattern:
       - Both models objects can be cloned\
     
    4- Filter Pattern
       - Applied in searching for messages and contacts\
     
    5- Marker Interface Pattern
       - in cloning objects , and serialization and deserialization of Json Objects\
     
The Design Decisions made:
    - Sending messages can be from one user to multiple users\
    - User can use only one custom folder to drag the needed files inside\
    - In searching for messages by "to" attribute if you put multiple users, you will get messages to either of them (inclusively)\
    
UML:
   ![uml 1](https://user-images.githubusercontent.com/95590176/191980036-1b915993-6d52-4269-950a-088ec8d5b00b.jpg)
    
   ![uml 2](https://user-images.githubusercontent.com/95590176/191980086-1ed2394a-dc4b-469a-9b72-c395a9911f04.jpeg)

   ![uml 3](https://user-images.githubusercontent.com/95590176/191980204-1010f436-bf4a-411c-8f69-caee5d94341e.png)
