This is the successor of the javascript antiraid bot. 

Messages sent are stored in a local cache file(json) for a period before deletion. Each messages sent are compared with each other based on a similarity score. 
A configurable option, allows the owner to change the similarity score based on how exact each messages are being compared. Such as from checking based on exact match to loosely matched. This is similar behaviour based on fuzzy matching. 


Example: If similarity score is set 0.8 then the following messages will be flagged and get sanctioned accordingly

Message 1: Hello world, how are you
Message 2: hello how are u?

The above example will get flagged then. 
This is useful when selfbots spam across multiple channels having 80% indentical message while 20% message as random generated numbers.

The comparison algorithm being used to compare messages based on similarity is "Dice coefficient", however can be used different algorithm such as Jaro winkler etc


TODO list in future: Store files in json which is ineffective, possible remedy is using redis as storage solution. 


If you need some questions either raise a ticket or contact me on discord Sasiko#123
