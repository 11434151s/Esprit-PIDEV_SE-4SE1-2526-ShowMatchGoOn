db.users.updateOne(
  {username: 'user'}, 
  {$set: {password: '$2a$10$v0cBK6wtwQZXoiasSGDJuekKN5/Q0UF4h9VrgWij1cPdAOsu.zvke'}}
);
print("User password updated to user123");
db.users.findOne({username: 'user'});
