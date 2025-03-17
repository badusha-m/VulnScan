import sqlite3
import os
import pickle

# Hardcoded credentials (Bad Practice)
USERNAME = "admin"
PASSWORD = "password123"

def authenticate(user, password):
    if user == USERNAME and password == PASSWORD:
        print("Authentication successful!")
    else:
        print("Authentication failed!")

def sql_injection_vulnerable(query):
    conn = sqlite3.connect(":memory:")
    cursor = conn.cursor()
    cursor.execute("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, password TEXT)")
    cursor.execute("INSERT INTO users (name, password) VALUES ('admin', 'adminpass')")
    
    # Vulnerable SQL query (no parameterized query, direct concatenation)
    cursor.execute("SELECT * FROM users WHERE name = '" + query + "'")
    print(cursor.fetchall())
    conn.close()

def command_injection_vulnerable(command):
    # Dangerous: Directly executing user input in system command
    os.system(command)

def insecure_deserialization(serialized_data):
    # Unsafe deserialization of untrusted data
    return pickle.loads(serialized_data)

def eval_execution(expression):
    # Arbitrary code execution vulnerability
    return eval(expression)

if __name__ == "__main__":
    user_input = input("Enter username: ")
    pass_input = input("Enter password: ")
    authenticate(user_input, pass_input)
    
    sql_query = input("Enter SQL query: ")
    sql_injection_vulnerable(sql_query)
    
    cmd = input("Enter command: ")
    command_injection_vulnerable(cmd)
    
    pickled_data = input("Enter pickled data: ").encode()
    insecure_deserialization(pickled_data)
    
    expr = input("Enter Python expression: ")
    print(eval_execution(expr))
