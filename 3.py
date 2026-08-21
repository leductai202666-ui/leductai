from google import genai
from google.genai import types
from PIL import Image
from PyPDF2 import PdfReader
import streamlit as st
import httpx
client = genai.Client(api_key = "")
with st.sidebar:
    st.write("Upload your file here")
    file = st.file_uploader("Upload your file here", type= ["pdf", "txt"])
st.title("Hello world")
x = st.text_area("Enter your prompt")
max_output_tokens = 500
y = st.button("Send")
if file is not None and x.strip():
    # 1. Khởi tạo chat inside if
    reaction = client.chats.create(
        model="gemini-3.7-flash",
    )
    
    # 2. Đọc file PDF (Bỏ các dấu phẩy ',' ở cuối dòng)
    pdf_reader = PdfReader(file)
    text = ""
    for page in pdf_reader.pages:
        extracted = page.extract_text()
        if extracted:
            text += extracted

    # 3. Gửi tin nhắn inside if (Tránh NameError)
    fen = reaction.send_message([text, x])
    st.write(fen.text)


        
if y is True and x.strip():
  chat = client.chats.create(
        model =  "gemini-3.7-flash",
        config= types.GenerateContentConfig(
            temperature = 0.1,
            max_output_tokens = 500,
            system_instruction = "You are a professional expert"


        )
    )
  response = chat.send_message(x)
  st.write(response.text) 
elif  y is False and x.strip():
    st.write("Please press button to send")
elif y is True and x.strip() == "":
    st.write("Please write something to Send")
elif y is False and x.strip() == "":
    st.write("Please write and press button to ask")

   
