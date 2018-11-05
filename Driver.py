from multiprocessing import Pool
from subprocess import call

num_threads = 4
def run_simulations(n):
    (name, num,mode)=n
    tag1=name+num+"M"+str(mode)+".out"
    tag2=name+num+"M"+str(mode)+".err"
    f1=open(tag1,'w+')
    f2=open(tag2,'w+')
    print("Starting "+tag1) 
    call(["bash","runGenProg.sh",name,num,str(mode)],stdout=f1,stderr=f2)
    #call(["bash","runGenProg.sh",name,num,str(mode)])

taskList = []

for i in range(3, 4):
    name="Chart"
    max=26
    if i==1 :
        name="Closure"
        max=133
    elif i==2:
        name="Lang"
        max=65
    elif i==3:
        name="Math"
        max=106
    elif i==4:
        name="Mockito"
        max=38
    elif i==5:
        name="Time"
        max=27
    #for j in [4,24,28,35,36,38,40,42,43,44,47,49,51,52,62,67,72,89,91,99,106]:
    for j in [62]:
        for k in range (0, 4):
            taskList.append((name,str(j),k))

p = Pool(num_threads)
p.map(run_simulations, taskList)
