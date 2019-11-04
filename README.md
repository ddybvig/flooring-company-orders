# flooring-company-orders

This is my mastery project for the object-oriented-programming portion of my Java Apprenticeship at the Software Guild. 
It is a Maven project written in NetBeans. I am very proud of all the work I put into this and how it turned out!

For previous projects I completed that persisted memory into a .txt file, all data was contained in one file. This project 
is slightly different in that the DAO generates a new file for each date. This was one of those things that sounds more
complicated than it actually is, but it was still fun to rig up.

There is very little hard coding in this program. Each order contains data that can change
for the flooring material chosen as well as the state the order originates from. All of that data is read from .txt files, which
means that data could easily be altered without having to change any of the actual code. It was hard to resist the temptation
to hard code some things, but when all is said and done you realize how important it is to avoid that!

This project also contains the only stream to be found in all of my projects. It generates a new order number in the OrderDao.
Getting more comfortable with streams and functional programming in general is one of my goals as I continue to practice
coding on my own.
