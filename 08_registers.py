import re

class Registers(object):

    regexp = r'^(\S+)\s([a-z]{3})\s([-0-9]+)\sif\s(\S+)\s([><=!]+)\s([-0-9]+)$'

    # b inc 5 if a > 1
    # a inc 1 if b < 5
    # c dec -10 if a >= 1
    # c inc -20 if c == 10
    def parse_line(self, line):
        m = re.match(Registers.regexp, line)
        if not m:
            print('UNMATCHED:', line)
            return None
        groups = m.groups()
        target = groups[0]
        target_operation = groups[1]
        target_change = groups[2]
        check_lhs = groups[3]
        check_operation = groups[4]
        check_rhs = groups[5]
        return (
            target,
            target_operation,
            int(target_change),
            check_lhs,
            check_operation,
            int(check_rhs),
        )

    def do_change(self, check_lhs, check_operation, check_rhs):
        check_lhs_value = self.registers[check_lhs] if check_lhs in self.registers else 0
        if check_operation == ">":
            return check_lhs_value > check_rhs
        if check_operation == "<":
            return check_lhs_value < check_rhs
        if check_operation == "==":
            return check_lhs_value == check_rhs
        if check_operation == "!=":
            return check_lhs_value != check_rhs
        if check_operation == ">=":
            return check_lhs_value >= check_rhs
        if check_operation == "<=":
            return check_lhs_value <= check_rhs
        raise ValueError("Invalid operation {}".format(check_operation))
        print(check_lhs, check_operation, check_rhs)
        assert(False)

    def adjust_register(self, target, target_operation, target_change):
        if target not in self.registers:
            self.registers[target] = 0
        if target_operation == 'inc':
            self.registers[target] += target_change
        elif target_operation == 'dec':
            self.registers[target] -= target_change
        else:
            raise ValueError("Invalid operation {}".format(target_operation))
        return self.registers[target]

    def solve(self, instructions):
        self.reg_max = 0
        self.registers = {}
        for instruction in instructions:
            parsed = self.parse_line(instruction)
            if parsed and self.do_change(parsed[3], parsed[4], parsed[5]):
                new_val = self.adjust_register(parsed[0], parsed[1], parsed[2])
                if new_val > self.reg_max:
                    self.reg_max = new_val
        return max(self.registers.values()), self.reg_max

lines = [
    'b inc 5 if a > 1',
    'a inc 1 if b < 5',
    'c dec -10 if a >= 1',
    'c inc -20 if c == 10',
]
with open('registers.txt') as fh:
    lines = fh.readlines()
    lines = [l.strip() for l in lines]

print(Registers().solve(lines))





