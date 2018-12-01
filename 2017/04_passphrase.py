def check_phrase(phrase):
    l = phrase.split(" ")
    l = [''.join(sorted(w)) for w in l]
    return len(l) == len(set(l))


with open("./passphrase.txt") as fh:
    phrases = fh.readlines()
phrases = [p.strip() for p in phrases]

sum([1 for p in phrases if check_phrase(p)])

