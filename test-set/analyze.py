import json
import random

SEED = 0
random.seed(a=SEED)

def separate_by_project(bugs_list):
    proj_to_bugs = dict()
    for b in bugs_list:
        b_proj_name = b["project"]
        if b_proj_name not in proj_to_bugs.keys():
            proj_to_bugs[b_proj_name] = list()
        proj_to_bugs[b_proj_name].append(b)

    list_of_lists = list()
    for p, b_list in proj_to_bugs.items():
        list_of_lists.append(b_list)
    return list_of_lists

def sample_three(bugs_list):
    if len(bugs_list) < 3:
        print("Length less than three")
        return
    return random.sample(bugs_list, 3)

def get_samples_byproj(bugs_byproj):
    samples_byproj = list()
    for bugs_list in bugs_byproj:
        samples_byproj.append(sample_three(bugs_list))
    return samples_byproj

filename = "defects4j-bugs.json"
bugs = json.load(open(filename))
bugs_single_repair = [b for b in bugs if len(b["repairActions"]) == 1]
bugs_multi_repairs = [b for b in bugs if len(b["repairActions"]) > 1]
bugs_multi_repairs_1failtest = [b for b in bugs_multi_repairs if len(b["failingTests"]) == 1]
bugs_multi_repairs_morefailtests = [b for b in bugs_multi_repairs if len(b["failingTests"]) > 1]

bugs_single_repair_byproj = separate_by_project(bugs_single_repair)
bugs_multi_repairs_1failtest_byproj = separate_by_project(bugs_multi_repairs_1failtest)
bugs_multi_repairs_morefailtests_byproj = separate_by_project(bugs_multi_repairs_morefailtests)

sampled_bugs_single_repair_byproj = get_samples_byproj(bugs_single_repair_byproj)
sampled_bugs_multi_repairs_1failtest_byproj = get_samples_byproj(bugs_multi_repairs_1failtest_byproj)
sampled_bugs_multi_repairs_morefailtests_byproj = get_samples_byproj(bugs_multi_repairs_morefailtests_byproj)

for L in sampled_bugs_single_repair_byproj:
    print([(b["project"], b["bugId"]) for b in L])

for L in sampled_bugs_multi_repairs_1failtest_byproj:
    print([(b["project"], b["bugId"]) for b in L])

for L in sampled_bugs_multi_repairs_morefailtests_byproj:
    print([(b["project"], b["bugId"]) for b in L])


outfilename = "bugs_set_single_repair.csv"
outfile = open(outfilename, "w")
outfile.write("Project, BugId, NumRepairActions, NumFailingTests\n")
for L in sampled_bugs_single_repair_byproj:
    for b in L:
        outfile.write(b["project"] + ", " + str(b["bugId"]) + ", " + str(len(b["repairActions"])) + ", " + str(
            len(b["failingTests"])) + "\n")
outfile.close()

outfilename = "bugs_set_multi_repairs_1failtest.csv"
outfile = open(outfilename, "w")
outfile.write("Project, BugId, NumRepairActions, NumFailingTests\n")
for L in sampled_bugs_multi_repairs_1failtest_byproj:
    for b in L:
        outfile.write(b["project"] + ", " + str(b["bugId"]) + ", " + str(len(b["repairActions"])) + ", " + str(
            len(b["failingTests"])) + "\n")
outfile.close()

outfilename = "bugs_set_multi_repairs_morefailtests.csv"
outfile = open(outfilename, "w")
outfile.write("Project, BugId, NumRepairActions, NumFailingTests\n")
for L in sampled_bugs_multi_repairs_morefailtests_byproj:
    for b in L:
        outfile.write(b["project"] + ", " + str(b["bugId"]) + ", " + str(len(b["repairActions"])) + ", " + str(
            len(b["failingTests"])) + "\n")
outfile.close()
