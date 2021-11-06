This is the successor of the javascript antiraid bot. 
Few changes has been made such as ignoring duplication check if the messags are less than a specific value. Helps when users just post emotes. 

Store files in json which is ineffective, possible remedy is using redis as storage solution. 

The comparison algorithm being used to compare messages based on similarity is "Dice coefficient", however can be used different algorithm such as Jaro winkler etc
