import pandas

df = pandas.read_csv('zoneData.csv')
states = []
for i in range(3):
    states.append("Andaman and Nicobar lslands")
for i in range(13):
    states.append("Andhra Pradesh")
for i in range(25):
    states.append("Arunachal Pradesh")
for i in range(33):
    states.append("Assam")
for i in range(38):
    states.append("Bihar")
for i in range(1):
    states.append("Chandigarh")
for i in range(27):
    states.append("Chhattisgarh")
for i in range(3):
    states.append("Dadra and Nagar Haveli and Daman and Diu")
for i in range(11):
    states.append("Delhi")
for i in range(2):
    states.append("Goa")
for i in range(33):
    states.append("Gujarat")
for i in range(22):
    states.append("Haryana")
for i in range(12):
    states.append("Himachal Pradesh")
for i in range(20):
    states.append("Jammu and Kashmir")
for i in range(24):
    states.append("Jharkhand")
for i in range(30):
    states.append("Karnataka")
for i in range(14):
    states.append("Kerala")
for i in range(2):
    states.append("Ladakh")
for i in range(1):
    states.append("Lakshadweep")
for i in range(52):
    states.append("Madhya Pradesh")
for i in range(36):
    states.append("Maharashtra")
for i in range(16):
    states.append("Manipur")
for i in range(11):
    states.append("Meghalaya")
for i in range(11):
    states.append("Mizoram")
for i in range(11):
    states.append("Nagaland")
for i in range(30):
    states.append("Odisha")
for i in range(4):
    states.append("Puducherry")
for i in range(22):
    states.append("Punjab")
for i in range(33):
    states.append("Rajasthan")
for i in range(4):
    states.append("Sikkim")
for i in range(37):
    states.append("Tamil Nadu")
for i in range(33):
    states.append("Telangana")
for i in range(8):
    states.append("Tripura")
for i in range(75):
    states.append("Uttar Pradesh")
for i in range(13):
    states.append("Uttarakhand")
for i in range(23):
    states.append("West Bengal")
size=0
for i in states:
    size+=1
print(size)
df.insert(1, "State", states, True)
print(df)
df.to_csv('zoneData.csv', index=False)
