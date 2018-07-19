from multiprocessing import Pool
from subprocess import call

num_threads = 40
def run_simulations(n):
    (name, num)=n
    
    call(["bash","runGenProg.sh",name,num])
    #f = open(tag,'w+')
    #f.write(st)
    #f.close()


taskList = []

for i in range (3, 4):
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
    for j in range (51, 52):
        taskList.append((name,str(j)))

p = Pool(num_threads)
p.map(run_simulations, taskList)
