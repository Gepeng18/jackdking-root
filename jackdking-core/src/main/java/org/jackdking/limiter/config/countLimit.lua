-- ����
-- ��ȡ����ǩ������
local key = KEYS[1]
-- ���ýű������������С
local limit = tonumber(ARGV[1])
-- ��ȡ��ǰ������С
local count = tonumber(redis.call('get',key) or "0")
-- �Ƿ񳬳�������ֵ
if count + 1 > limit then
    -- �ܾ�����
    return false
else
    -- û�г�����ֵ
    -- ���õ�ǰ���ʵ����� + 1
    redis.call("incr",key)
    -- ���ù���ʱ��
    redis.call("expire",key,ARGV[2])
    return true
end